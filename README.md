# Getting Started

### Docker Build
```
docker build -t seojuwon/proto-spring-helm-api:0.0.7 .
cle
```
### Helm Commend Install Or UnInstall
```
helm install proto-spring-helm-api ./proto-spring-helm-api  -n proto
```

### Kubectl
```
kubectl get pods -n proto
kubectl get svc -n proto
kubectl logs -f proto-spring-helm-api-745bf77bb9-n59bv -n proto // 실시간로그 
kubectl describe pod proto-spring-app-deployment-78c868545b-stmr6 -n proto // 파드 생성실패 시 확인 
kubectl exec -it proto-spring-helm-api-6fdd78d86c-czg92 -n proto -- /bin/bash

git clone https://github.com/juwonseo84/proto-spring-helm-api.git
helm uninstall proto-spring-app -n proto
helm uninstall proto-spring-helm-api -n proto
helm install proto-spring-helm-api ./root/helm-cli/proto-spring-helm-api/charts -n proto
helm install proto-spring-helm-api ./charts -n proto
```

## Request
curl --location 'http://192.168.20.15:31631/helm/installs' \
--header 'Content-Type: application/json' \
--data {
"releaseName": "proto-spring-app",
"gitUrl": "https://github.com/juwonseo84/proto-spring-app.git",
"branch": "master",
"token": "-",
"namespace": "proto",
"chartPath": "charts",
"imageTag": "0.0.1"
}'
curl --location 'http://192.168.20.15:31631/helm/upgrade' \
--header 'Content-Type: application/json' \
--data {
"releaseName": "proto-spring-app",
"gitUrl": "https://github.com/juwonseo84/proto-spring-app.git",
"branch": "master",
"token": "",
"namespace": "proto",
"chartPath": "charts",
"imageTag": "0.0.3"
}
```
///////
