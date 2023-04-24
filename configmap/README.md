# Configuration

Not everything that is required to run our java application is typically going to be included in our jar artifact.
There's usually all kinds of configuration which we want to separate from the application,
because for instance we might want to run multiple instances with different configuration.

This is one of the factors of 12factor apps. https://12factor.net/

SHOW: application

## ConfigMap

A **ConfigMap** is a kubernetes resource used to store non-confidential data in key-value pairs. 
Pods can consume ConfigMaps as environment variables or as files in a volume.

A **ConfigMap** allows you to decouple environment-specific configuration from your container images, so that your applications are easily portable.
For example, you could add the hostnames or URLs of external services to a configmap so that in development you can talk 
to the development environment of the external service or even your own stubs, while in production you want to communicate with the real service.

SHOW: configmap yaml

Our ConfigMap contains one key-value pair: application-kubernetes.yaml with as value spring application properties in yaml format. 
We can then mount this as a file in our deployment after which our spring boot application can pick it up and load it in if the *kubernetes* profile is enabled.

Another way to configure spring properties via the ConfigMap would be using them as environment variables.
For example to set the `spring.jpa.database-platform: org.hibernate.dialect.MySQL8Dialect`, we could add following key-value pair:

```yaml
SPRING_JPA_DATABASEPLATFORM: org.hibernate.dialect.MySQL8Dialect
```

Note: the dash in database-platform is not there anymore, as environment variables do not support this. 
This however still works and is an example of **relaxed binding** in spring boot. 
https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-external-config.html#boot-features-external-config-relaxed-binding

### Using ConfigMaps

#### Mounting all the keys in the configmap as a file in a directory */app/resources*:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          ...
          volumeMounts:
            - name: my-config
              mountPath: /app/resources
      volumes:
        - name: my-config
          configMap:
            name: java-service-configmap
```

#### Mounting a specific key in the configmap as a file:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          ...
          volumeMounts:
            - name: my-config
              mountPath: /app/resources/application-kubernetes.yaml
              subPath: application-kubernetes.yaml
      volumes:
        - name: my-config
          configMap:
            name: java-service-configmap
```

#### Mounting all the keys in the configmap as environment variables:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          envFrom:
            - configMapRef:
                name: java-service-configmap
```


#### Mounting a specific key in the configmap as an environment variable:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          ...
          env:
            - name: SPRING_JPA_DATABASEPLATFORM
              valueFrom:
                configMapKeyRef:
                  name: java-service-configmap
                  key: SPRING_JPA_DATABASEPLATFORM
```

## Secret

Secrets are similar to ConfigMaps but are specifically intended to hold confidential data.

They also consist of key-value pairs but the values are specified base64 encoded.
It is also possible to specify values as plaintext by putting the key-values under stringData instead of data.
Kubernetes will automatically encode and add them to the data section.

Base64 is not encryption and anyone can decode it! The reason that Secret holds values in base64 format is
so that they can contain binary data. e.g. certificates

Note: ConfigMaps can also contain binary data since kubernetes 1.10 by adding them under the *binaryData* key.

It is up to the kubernetes cluster administrators to secure access to Secrets by encrypting them at rest
and configuring RBAC (not everyone with access to the cluster can access all resources).

SHOW: secret yaml

### Using Secrets


#### Mounting all the keys in the secret as a file in a directory */app/resources*:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          ...
          volumeMounts:
            - name: my-secret
              mountPath: /app/resources
      volumes:
        - name: my-secret
          secret:
            secretName: java-service-secret
```

#### Mounting a specific key in the secret as a file:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          ...
          volumeMounts:
            - name: my-secret
              mountPath: /app/resources/apikey.txt
              subPath: USERSERVICE_APIKEY
      volumes:
        - name: my-secret
          secret:
            secretName: java-service-secret
```

#### Mounting all the keys in the secret as environment variables:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          envFrom:
            - secretRef:
                name: java-service-secret
```


#### Mounting a specific key in the secret as an environment variable:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: java-service-deployment
  labels:
    app: java-service
spec:
  ...
  template:
    ...
    spec:
      containers:
        - name: java-service
          ...
          env:
            - name: USERSERVICE_APIKEY
              valueFrom:
                secretKeyRef:
                  name: java-service-secret
                  key: USERSERVICE_APIKEY
```

SHOW: deployment.yaml

```bash
kubectl apply -f ./configmap/deployment
```

SHOW: demo
