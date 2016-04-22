## IBM Watson Health Cloud FHIR Server

### Overview
The **fhir** git project contains the implementation of a REST API patterned after
the [FHIR REST API specification](https://www.hl7.org/fhir/http.html).

The FHIR Server implements operations on various FHIR-defined resource types
(e.g. Patient, Observation, etc.).

CODE_REMOVED


**Note to developers:**  
To associate a commit with an RTC work item (task, defect, etc.), add "workitem nnnnnn -" on the front of your commit message, like this:  

        git commit -m "workitem 123456 - Finished the FHIR Server code"  
        (ignore any error messages that start with "remote:")
        
If you forget to include your work item number in your commit message, you can always "amend" the commit message
by running this command prior to pushing your changes to the server:  

        git commit --amend  
        (this will open up your editor and allow you to change the commit message)
        
