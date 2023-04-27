# Java Guild Kubernetes

## Prerequisites

Following programs are necessary/handy when following this tutorial:

* [kubectl](https://kubernetes.io/docs/tasks/tools/)
* [minikube](https://kubernetes.io/docs/tasks/tools/)
* [Lens](https://k8slens.dev/)

## Objective

During the tutorial we will go over the main components of Kubernetes and demonstrate them by developing a java
application.

> **Note**: If you want to create your own docker images, then you'll need to add the following lines to your settings.xml
```xml
  <servers>
    <server>
        <id>registry.hub.docker.com</id>
        <username>USERNAME</username>
        <password>PASSWORD</password>
    </server>
  </servers>
```

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

If wanted, you can now change the number of replicas (which would create additional pods).

Once you are done, you can delete the deployment:

```bash
kubectl delete -f ./pod/deployment/deployment.yml
```
