## Volumes

On-disk files in a container are ephemeral, which presents some problems:
* loss of files when a container crashes
* sharing files between containers running in the same pod

The solution to this is kubernetes volumes, these are similar to the concept of Docker volumes but with more functionality.
In docker a volume is a directory on disk or in another container, while kubernetes supports many types of volumes.
Some of which are ephemeral and follow the lifetime of a pod, others are persistent and are not destroyed when a pod ceases to exist.

A **PersistentVolume** resource refers to storage in the cluster that has been provisioned by either an administrator or dynamically through a **StorageClass**. 
It contains the details of the implementation of the storage such as: NFS path, ISCSI target, S3 bucket or many more.

A **PersistentVolumeClaim** is a request for storage by a user. It can request specific sizes, the **StorageClass** name and access modes (e.g. they can be mounted ReadWriteOnce, ReadOnlyMany or ReadWriteMany)
This allows the user to consume abstract storage resources, where kubernetes will create the linked PersistentVolume containing where exactly it will be stored.

The **StorageClass** contains details and the name of the storage provisioner. e.g. an nfs provisioner with the network path to the nfs server

