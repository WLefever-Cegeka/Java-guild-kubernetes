## Volumes

On-disk files in a container are ephemeral, which presents some problems:
* loss of files when a container crashes
* sharing files between containers running in the same pod

The solution to this is kubernetes volumes, these are similar to the concept of Docker volumes but with more functionality.
In docker a volume is a directory on disk or in another container, while kubernetes supports many types of volumes.

A **PersistentVolume** resource refers to storage in the cluster that has been provisioned by either an administrator or dynamically through a **StorageClass**. 
It contains the details of the implementation of the storage such as: NFS path, local storage on the kubernetes node, S3 bucket or many more.

A **PersistentVolumeClaim** is a request for storage by a user. It can request specific sizes, the **StorageClass** name and access modes (e.g. they can be mounted ReadWriteOnce, ReadOnlyMany or ReadWriteMany)
This allows the user to consume abstract storage resources, where kubernetes will create the linked PersistentVolume containing where exactly it will be stored.

The **StorageClass** contains details and the name of the storage provisioner. e.g. an nfs provisioner with the network path to the nfs server
These are provisioned by the cluster administrators.

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: stateful-java-service-data
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 64Mi
  storageClassName: standard
```

### StatefulSets

When deploying a stateful application in kubernetes, we use a **StatefulSet** instead of a **Deployment**.
Unlike a Deployment, a StatefulSet maintains a sticky identity for each of its Pods. 
This means that when we scale horizontally, multiple pods are not writing over each-others changes

We will not be creating the **PersistentVolumeClaim** resources ourselves as we would need to create as many as we have pods and we want to be able to scale dynamically.
StatefulSet allows us to specify **volumeClaimTemplates** to dynamically create these claims.

SHOW: statefulset.yml

SHOW: demo
