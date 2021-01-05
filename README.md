# Video Uploading Microservice

## Google Cloud Storage Authentication

On Kubernetes:

```bash
kubectl create secret generic gcloud-storage-key --from-file=key.json=PATH_TO_KEY.json
```

```bash
kubectl create secret generic gcloud-storage-api \
    --from-literal=project_id=PROJECT_ID \
    --from-literal=bucket_name=BUCKET_NAME \
    --from-file=key.json=PATH_TO_KEY.json
```

Locally:

```bash
GOOGLE_APPLICATION_CREDENTIALS=PATH_TO_KEY.json
```
