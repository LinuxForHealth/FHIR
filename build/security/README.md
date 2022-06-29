# Application Security Testing workflow

The artifacts are used to execute a variety of analyses:
- Dynamic application security testing 
- Static application security testing 
- Interactive Application Security Testing
 
The workflow is configured with a set of scanners. The scanners are configured using an environment variable `SCANNERS`.  The SCANNERS variable supports the following values: 'dockle', 'asoc', 'trivy'. 'contrast' and 'sonarqube' are experimental, and not fully enabled.

The workflow is setup with the `build/security/setup.sh`, run with `build/security/run.sh` and the logs from `build/security/logs/output/` are saved as build artifacts.

Events execute the `Application Security Testing` workflow are: 
- Pull Requests with the label `security`
- Weekly on Saturday (specified in the crontab format)

## AppScan on Cloud (asoc)

The [AppScan on Cloud](https://help.hcltechsw.com/appscan/ASoC/src_cli_linux_analysis.html#src_cli_linux_analysis__queue_analysis) enables the scanning of the IBM FHIR Server runtime bytecode. The automation wrapped around this analysis limits the surface area to IBM FHIR Server developed application code. The Implementation Guides are not scanned. The output is user generated, and the `irx` file is saved in the artifact output.

### Generating the Latest Report (team members only)
The following steps under this section are designed to aid the team members in generating the security scan artifacts and reports. A link to the current application is at [scan](https://cloud.appscan.com/AsoCUI/serviceui/main/myapps/oneapp/c4658cf4-e742-4410-99d4-d1c4084b856e/scans)

#### Generating the Default Report

- Navigate to https://cloud.appscan.com/AsoCUI/serviceui/main/myapps/portfolio
- Search for `IBM FHIR Server`, and click on the entry.
- Click Scan Report 
- Select all of the entries in `Include Meta Data`
- Select PDF
- Click Run Report

#### Generating the Industry Standard Report

- Navigate to https://cloud.appscan.com/AsoCUI/serviceui/main/myapps/portfolio
- Search for `IBM FHIR Server`, and click on the entry.
- Click Scan Report 
- Select Industry Standard
- Select Type of Report (such as `CWE/SANS Top 25 Most Dangerous Errors`)
- Select PDF
- Click Run Report

### Running Locally

- Navigate to https://cloud.appscan.com/AsoCUI/serviceui/main/admin/apiKey
- Click Generate
- Copy the values out into a secure location. 

- Open a terminal and export the values (modified for your application)

```
export APPSCAN_KEY=6-7-8-9-10
export APPSCAN_SECRET=THE_SECRET
export APPSCAN_APPID=1-2-3-4-5
```
- Run locally with `build/security/asoc.sh`

The analysis sends an email when complete, and ready for report generation.

## Updating the workflow secrets

The project may need to update the values `APPSCAN_KEY`, `APPSCAN_SECRET` and `APPSCAN_APPID`. These values may need to be changed as keys and secrets change. 

- Navigate to https://github.com/LinuxForHealth/FHIR/settings/secrets/new
- Select the value to update 
- Click Update
- Click Update Secret

You are now good to go.

<hr>

## Dockle

The automation uses [Dockle](https://github.com/goodwithtech/dockle) to generate a linting report/output which is useful to connect any issues related to the docker build.  The output files is `dockle.json`.

<hr>

## Trivy
[Trivy](https://github.com/aquasecurity/trivy#embed-in-dockerfile) is used to check for vulnerabilities in the IBM FHIR Server container.

<hr>

Sonarqube and contrast are also in the build pipeline, and require specific enablement in the environment.
