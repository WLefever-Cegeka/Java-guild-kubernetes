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
mvn clean compile jib:dockerBuild
```

[//]: # (mvn clean compile jib:build is used to push to a registry)

Apply the deployment file:
```
kubectl apply -f ./pod/deployment/deployment.yml
```