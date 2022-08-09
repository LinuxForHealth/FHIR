These examples are support IBM FHIR Server's Bulk Data feature and dynamic group export.

The examples exercise the following features of dynamic groups:

age-range-blood-pressure-group
Age >= 35 AND Age <= 70 which is outside the acceptable range

age-simple-group
Age >= 30

age-range-group
Age >= 13 AND Age <= 56

age-simple-disabled-group
Age >= 30 which is disabled

age-range-with-gender-group
Age >= 13 AND Age <= 56 AND GENDER = Female

age-range-with-gender-and-exclude-group
Age >= 13 AND Age <= 56 AND GENDER = Female AND NOT Pregnant

# Bulk Data - Groups: Location - Data Source
To support Bulk Data tests, the location folder includes generated Location resources. These generated location resources are specific to Synthetic Mass. The list of locations in location-data.csv is generated from [SyntheticMass data](https://synthea.mitre.org/downloads) and the locations have been mapped to latitudes and longitudes. The list was cleansed to normalize the zip codes to five digits. 

To see a map of the locations, click on the [link](https://drive.google.com/open?id=1LyXzxxe6xr69a58rRWtitMjo1_U_uEXh&usp=sharing).
