ExtensionSearchTool.java in fhir-search is used to generate the basic JSON file. 
The JSON file is then copied into each config location, and modified to match the intended type (per the folder). The default config is modified by hand, where each Bundle.Entry is changed to match the proper type.  

Should an XML format be needed, you should modify ExtensionSearchTool to output XML (when XML format is enabled for the project). 
