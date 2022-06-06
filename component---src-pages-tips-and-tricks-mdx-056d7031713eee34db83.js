(window.webpackJsonp=window.webpackJsonp||[]).push([[42],{INPU:function(e,t,n){"use strict";n.r(t),n.d(t,"_frontmatter",(function(){return b})),n.d(t,"default",(function(){return l}));n("tkto"),n("yXV3"),n("pNMO"),n("zKZe"),n("q1tI");var r=n("7ljp"),o=n("013z"),c=(n("qKvR"),["components"]);function p(){return(p=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&(e[r]=n[r])}return e}).apply(this,arguments)}function a(e,t){if(null==e)return{};var n,r,o=function(e,t){if(null==e)return{};var n,r,o={},c=Object.keys(e);for(r=0;r<c.length;r++)n=c[r],t.indexOf(n)>=0||(o[n]=e[n]);return o}(e,t);if(Object.getOwnPropertySymbols){var c=Object.getOwnPropertySymbols(e);for(r=0;r<c.length;r++)n=c[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(o[n]=e[n])}return o}var b={},s={_frontmatter:b},i=o.a;function l(e){var t=e.components,n=a(e,c);return Object(r.b)(i,p({},s,n,{components:t,mdxType:"MDXLayout"}),Object(r.b)("p",null,"These are the tips and tricks working with the IBM FHIR Server. "),Object(r.b)("h1",null,"DB2 Error 42501"),Object(r.b)("p",null,"If you see the 42501 error in the fhir-server logs, this section walks you through how to confirm and verify the permissions: "),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre"},"42501   The authorization ID does not have the privilege to perform the specified operation on the identified object.\n")),Object(r.b)("p",null,"Check the Stored Procedure and object mapping"),Object(r.b)("p",null,Object(r.b)("inlineCode",{parentName:"p"},"SELECT * FROM syscat.procedures WHERE procschema = 'FHIRDATA'")),Object(r.b)("p",null,"The response shows the SQL objects (third column): "),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre"}," SCHEMA     TABLE              OBJECTNAME\n FHIRDATA   ADD_CODE_SYSTEM    SQL191105170018025        84725 BLUADMIN          2 0x38001800                             Q      2019-11-05\n FHIRDATA   ADD_PARAMETER_NAME SQL191105170018126        84726 BLUADMIN          2 0x38001800                             Q      2019-11-05\n FHIRDATA   ADD_ANY_RESOURCE   SQL191105170018328        84728 BLUADMIN          9 0x380038007c006c003c003800380018001400 Q      2019-11-05\n")),Object(r.b)("p",null,"Check if the FHIRSERVER user has execute privelege on the objectname. "),Object(r.b)("pre",null,Object(r.b)("code",{parentName:"pre"},"select substr(authid,1,20) as authid\n    , authidtype\n    , privilege\n    , grantable\n    , substr(objectschema,1,12) as objectschema\n    , substr(objectname,1,30) as objectname\n    , objecttype \nfrom sysibmadm.privileges\nwhere objectschema not like 'SYS%' AND AUTHID='FHIRSERVER' AND PRIVILEGE = 'EXECUTE' \n")),Object(r.b)("p",null,"If missing, then you’ll want to run a grant\n",Object(r.b)("inlineCode",{parentName:"p"},"GRANT EXECUTE ON PROCEDURE FHIRDATA.ADD_ANY_RESOURCE TO FHIRSERVER")),Object(r.b)("p",null,"Confirm the procedure and re-execute.  "),Object(r.b)("p",null,Object(r.b)("a",{parentName:"p",href:"https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/seca/src/tpc/db2z_grantprivilege4executesp.html"},"https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/seca/src/tpc/db2z_grantprivilege4executesp.html")))}l.isMDXComponent=!0}}]);
//# sourceMappingURL=component---src-pages-tips-and-tricks-mdx-056d7031713eee34db83.js.map