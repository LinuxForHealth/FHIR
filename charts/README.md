# The IBM FHIR Server Helm Chart
[Helm](https://helm.sh/) is a technology for installing software on [Kubernetes](https://kubernetes.io/).

## Prerequisites
To install the IBM FHIR Server via helm, you must first have a database.

To install a PostgreSQL database to the same cluster using helm, you can run the following command:
```
$ helm install my-release bitnami/postgresql
```

For example if you target the default namespace and name the release "postgres",
then that would produce something like this:
```sh
$ helm install postgres bitnami/postgresql
NAME: postgres
LAST DEPLOYED: Wed Jun 23 12:01:11 2021
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
** Please be patient while the chart is being deployed **

PostgreSQL can be accessed via port 5432 on the following DNS name from within your cluster:

    postgres-postgresql.default.svc.cluster.local - Read/Write connection

To get the password for "postgres" run:

    export POSTGRES_PASSWORD=$(kubectl get secret --namespace default postgres-postgresql -o jsonpath="{.data.postgresql-password}" | base64 --decode)

To connect to your database run the following command:

    kubectl run postgres-postgresql-client --rm --tty -i --restart='Never' --namespace default --image docker.io/bitnami/postgresql:11.12.0-debian-10-r23 --env="PGPASSWORD=$POSTGRES_PASSWORD" --command -- psql --host postgres-postgresql -U postgres -d postgres -p 5432



To connect to your database from outside the cluster execute the following commands:

    kubectl port-forward --namespace default svc/postgres-postgresql 5432:5432 &
    PGPASSWORD="$POSTGRES_PASSWORD" psql --host 127.0.0.1 -U postgres -d postgres -p 5432
```
