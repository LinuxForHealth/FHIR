# Group: Descriptive

These descriptive groups are used as part of the support for Descriptive Groups, also known as Dynamic Groups or Cohorts.

The code in this package supports the development of the BulkData Group Export feature. [BulkData STU1](http://hl7.org/fhir/uv/bulkdata/export/index.html)

The package builds in the following use cases:

1. [AgeRangeExample](AgeRangeExample.java)
    * All Patients within a Range. These age ranges
    * No Nesting
1. [MultipleCharacterisitcsExample](MultipleCharacterisitcsExample.java)
1. [Wellness Annual](AnnualWellnessExample.java)
1. [Annual OB-GYN](AnnualObGynExample.java)
1. [Well Child Exam](WellChildExample.java)
1. [Breast Cancer Screening](BreastCancerScreeningExample.java)
1. [Cervical Cancer Screening](CervicalCancerScreeningExample.java)
1. [Colorectal Cancer Screening]()
1. [Diabetes]()
1. [Asthma]()
1. [Heart Failure]()
1. [Hypertension]()
1. [Diabetes Type II]()

https://www.hl7.org/fhir/group-example-patientlist.json.html
https://www.hl7.org/fhir/group-example-patientlist.json.html

Profiles: 
http://hl7.org/fhir/group-profiles.html
http://hl7.org/fhir/groupdefinition.html

If a FHIR server supports Group-level data export, it SHOULD support reading and searching for Group resource. This enables clients to discover available groups based on stable characteristics such as Group.identifier.

Note: How these Groups are defined is specific to each FHIR system’s implementation. For example, a payer may send a healthcare institution a roster file that can be imported into their EHR to create or update a FHIR group. Group membership could be based upon explicit attributes of the patient, such as age, sex or a particular condition such as PTSD or Chronic Opioid use, or on more complex attributes, such as a recent inpatient discharge or membership in the population used to calculate a quality measure. FHIR-based group management is out of scope for the current version of this implementation guide.


`http://hl7.org/fhir/StructureDefinition/groupdefinition`

 s (Cohorts)

The following are test groups: 

- 1 [Wellness Annual](AnnualWellnessExample.java)
- 2 [Annual OB-GYN](AnnualObGynExample.java)
- 3 [Well Child Exam](WellChildExample.java)
- 4 [Breast Cancer Screening](BreastCancerScreeningExample.java)
- 5 [Cervical Cancer Screening](CervicalCancerScreeningExample.java)
- 6 [Colorectal Cancer Screening]()
- 7 [Diabetes]()
- 8 [Asthma]()
- 9 [Heart Failure]()
- 10 [Hypertension]()
- 11 [Diabetes Type II]()
    - Age: 18 >=    < 75
    - Diagnosis for diabetes: ICD-10-CM
    
    ```
    E10.10, E10.11, E10.21, E10.22, E10.29, E10.311, E10.319, E10.3211, E10.3212, E10.3213, E10.3219, E10.3291, E10.3292, E10.3293, E10.3299, E10.3311, E10.3312, E10.3313, E10.3319, E10.3391, E10.3392, E10.3393, E10.3399, E10.3411, E10.3412, E10.3413, E10.3419, E10.3491, E10.3492, E10.3493, E10.3499, E10.3511, E10.3512, E10.3513, E10.3519, E10.3521, E10.3522, E10.3523, E10.3529, E10.3531, E10.3532, E10.3533, E10.3539, E10.3541, E10.3542, E10.3543, E10.3549, E10.3551, E10.3552, E10.3553, E10.3559, E10.3591, E10.3592, E10.3593, E10.3599, E10.36, E10.37X1, E10.37X2, E10.37X3, E10.37X9, E10.39, E10.40, E10.41, E10.42, E10.43, E10.44, E10.49, E10.51, E10.52, E10.59, E10.610, E10.618, E10.620, E10.621, E10.622, E10.628, E10.630, E10.638, E10.641, E10.649, E10.65, E10.69, E10.8, E10.9, E11.00, E11.01, E11.21, E11.22, E11.29, E11.311, E11.319, E11.3211, E11.3212, E11.3213, E11.3219, E11.3291, E11.3292, E11.3293, E11.3299, E11.3311, E11.3312, E11.3313, E11.3319, E11.3391, E11.3392, E11.3393, E11.3399, E11.3411, E11.3412, E11.3413, E11.3419, E11.3491, E11.3492, E11.3493, E11.3499, E11.3511, E11.3512, E11.3513, E11.3519, E11.3521, E11.3522, E11.3523, E11.3529, E11.3531, E11.3532, E11.3533, E11.3539, E11.3541, E11.3542, E11.3543, E11.3549, E11.3551, E11.3552, E11.3553, E11.3559, E11.3591, E11.3592, E11.3593, E11.3599, E11.36, E11.37X1, E11.37X2, E11.37X3, E11.37X9, E11.39, E11.40, E11.41, E11.42, E11.43, E11.44, E11.49, E11.51, E11.52, E11.59
    E11.610, E11.618, E11.620, E11.621, E11.622, E11.628, E11.630, E11.638, E11.641, E11.649, E11.65, E11.69, E11.8, E11.9, E13.00, E13.01, E13.10, E13.11, E13.21, E13.22, E13.29, E13.311, E13.319, E13.3211, E13.3212, E13.3213, E13.3219, E13.3291, E13.3292, E13.3293, E13.3299, E13.3311, E13.3312, E13.3313, E13.3319, E13.339, E13.3391, E13.3392, E13.3393, E13.3399, E13.3411, E13.3412, E13.3413, E13.3419, E13.3491, E13.3492, E13.3493, E13.3499, E13.3511, E13.3512, E13.3513, E13.3519, E13.3521, E13.3522, E13.3523, E13.3529, E13.3531, E13.3532, E13.3533, E13.3539, E13.3541, E13.3542, E13.3543, E13.3549, E13.3551, E13.3552, E13.3553, E13.3559, E13.3591, E13.3592, E13.3593, E13.3599, E13.36, E13.37X1, E13.37X2, E13.37X3, E13.37X9, E13.39, E13.40, E13.41, E13.42, E13.43, E13.44, E13.49, E13.51, E13.52, E13.59, E13.610, E13.618, E13.620, E13.621, E13.622, E13.628, E13.630, E13.638, E13.641, E13.649, E13.65, E13.69, E13.8, E13.9, O24.011, O24.012, O24.013, O24.019, O24.02, O24.03, O24.111, O24.112, O24.113, O24.119, O24.12, O24.13, O24.311, O24.312, O24.313, O24.319, O24.32, O24.33, O24.811, O24.812, O24.813, O24.819, O24.82, O24.83
    ```
    - AND 
    - Patient encounter during performance period (CPT or HCPCS)
    
    ```
    97802, 97803, 97804, 99201, 99202,
    99203, 99204, 99205, 99211, 99212, 99213, 99214, 99215, 99217, 99218, 99219, 99220, 99221, 99222,
    99223, 99231, 99232, 99233, 99238, 99239, 99281, 99282, 99283, 99284, 99285, 99291, 99304, 99305,
    99306, 99307, 99308, 99309, 99310, 99315, 99316, 99318, 99324, 99325, 99326, 99327, 99328, 99334,
    99335, 99336, 99337, 99341, 99342, 99343, 99344, 99345, 99347, 99348, 99349, 99350, G0270, G0271,
    G0402, G0438, G0439
    ```

    Actual Group http://hl7.org/fhir/actualgroup.html
    