# Java Guild Kubernetes

## Prerequisites

Following programs are necessary/handy when following this tutorial:

* [kubectl](https://kubernetes.io/docs/tasks/tools/)
* [minikube](https://kubernetes.io/docs/tasks/tools/)
* [Lens](https://k8slens.dev/)

## Objective

During the tutorial we will go over the main components of Kubernetes and demonstrate them by developing a java
application.

## Tutorial

### Step 1: Pod

#### Pod

A pod is an abstraction over a container (e.g. a Docker container) and is the smallest possible unit on which actions can be performed in Kubernetes.
An important aspect of pods is that they are not meant to live forever necessarily, i.e. if they are broken they are killed and a new pod is spun up.
Kubernetes assigns to each pod an ip address which becomes unavailable when the pod 'dies'.

####

#### Deployment step 1

Run the following command to create a docker image with name ``:
```
mvn clean compile jib:build
```

[//]: # (mvn clean compile jib:build is used to push to a registry)

Verify you are connected to the kubernetes cluster you intend to use with
```bash
kubectl config current-context
```

Apply the deployment file:
```bash
kubectl apply -f ./pod/deployment/deployment.yml
```

### Step 2: Service

We add a simple Spring RestController so that our mini app has something to respond to: see HelloService.java

to provide network access to the pod(s) that will expose the /hello endpoint, we create a service with

```bash
kubectl apply -f ./pod/deployment/service.yml
```

This service will forward traffic to one of the pods that have a matching label selector. Note this part in the service.yml

```yaml
selector:
    app: java-service
```

which matches this part in the deployment.yml:

```yaml
labels:
  app: java-service
```

#### Let's test the service

We used the NodePort type of service.
This means that k8s will look for an available port on the host machine and bind the service to that port.
You can find out which port was chosen with Lens or 

```bash
kubectl get service
```
> **Side note:** Each k8s resource has a long and a short name. svc is the shortname for service, so 
> ```bash
> kubectl get svc
> ```
> is exactly the same

now your browser should say Hello when you go to http://localhost:<port>/hello

Services are also exposed to the local k8s dns in this format (this is sometime slightly different depending on your provider):

```
<service name>.<namespace>.svc.cluster.local
```

So in our example from inside the cluster this address should say Hello:
> http://java-service.default.svc.cluster.local/hello

Now how do you get *inside* the cluster?
We need a pod that has curl. Easy-peasy: just deploy&run one with this command:

```bash
kubectl run curltest --image=curlimages/curl -i --tty -- sh
```

Now you can test the local url of the service with

```bash
curl http://java-service.default.svc.cluster.local/hello
```

#### What is this service thing actually doing?

When deploying multiple replicas of the pod, the service will start distributing the traffic over the available pods.
Also when updating the application, you deploy a new version of the pod without downtime:
The default **Strategy Type** for Deployments is **RollingUpdate**. You can verify this with Lens or with 

```bash
kubectl get deployments -o yaml
```

This means that k8s will make sure that at all times at least the number of desired replicas are available.
So with 1 desired replica, first a new pod will be added with the new version of the app. 
When the new pod (with v2 of your app) is running the service will start routing traffic to the new pod and k8s can now start removing the old pod

This strategy obviously requires the 2 version of the app to be compatible for your users/consumers.
If you cant have 2 versions running at the same time, use the **Recreate** strategy. Pods will be first all killed, and only recreated afterwards and you have a short downtime

### Step 3 : LoadBalancer: expose the service to the outside world.
With the **Nodeport** we have to know which port was opened by the cluster and on which node in the cluster to access it from outside. 
We can change the type to **LoadBalancer** to bind the service to the default load balancer provided by your k8s provider (if they provide one).

Let's do this:
* Update the service type in service.yml to LoadBalancer
* deploy the change with:

```bash
kubectl apply -f ./pod/deployment/service.yml
```
Check the result in Lens or with
```bash
kubectl set svc
```
It may take a while, depending on the provider. LoadBalancers are created asynchronously. 
When done, you will see now that the service will be exposed on an **external** address.
We can now use are browser to see Hello on the external address.

> On docker-desktop the external address is localhost, on AWS it will be some generated .awsservices.com address.

See on my machine http://localhost/hello works, if port 80 is not in use yet :)
