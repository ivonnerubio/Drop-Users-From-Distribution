def execute(){ 
  
  // Fetch current document details
  DocumentVO document = documentService.getDocumentDetails();  
  String tempRoleMap = "";

  //Prepare ADD/REMOVE Recipients JSON
  JsonObject responseJSON = new JsonObject();        

  // ADD/REMOVE USERS
  JsonArray usersList = new JsonArray();
  setDistribution(document.getPublisherEmail(), Recipient.USER, Recipient.REMOVE, usersList); // Params: User Email, Type Of Recipient, ADD/REMOVE, listObject.
  

// FIND USERS WITH THE ROLE BELOW AND ADD/REMOVE THEM FROM THE DISTRIBUTION LIST
  String roleName = "09 Contractor,09 Design Build Contractor,10 Sub Contractor";
  
  //create the distribution list and add the role name
  List<String> distributionRoleList = new ArrayList<String>();
  List<String> finalDistributionRoleList = new ArrayList<String>();
  if(roleName != null && !roleName.isEmpty() && roleName.contains(",")){

  distributionRoleList = roleName.split(",").toList()

  } else{
    

  distributionRoleList.add(roleName);

  }

  distributionRoleList.each {String it->

  it = it.trim();

  finalDistributionRoleList.add(it)

  }


  //create a distribution map and add the distribution list on it
  Map<String, List<String>> distributionMap = new HashMap();
  distributionMap.put(IGroovyConstant.DISTRIBUTION_ROLES, finalDistributionRoleList);

  // get users email address with the role previously selected
  Map<String, Map<String, List<String>>> RecipientUsersEmailIds  = documentService.getUsersEmailIdByGroupOrRole(distributionMap);

    Map<String,List<String>> RolesEmailMap =  RecipientUsersEmailIds.get(IGroovyConstant.DISTRIBUTION_ROLES);

  	// creates a for loop that will go through the rolemap and each one of the values. then it will get the list remove, convert to string and clean up the string to just have the role email addresses. 
    for(Map.Entry<String, List<String>> roleMap  :RolesEmailMap.entrySet()){
      tempRoleMap += roleMap.getValue().toString()+",";
    }
 	tempRoleMap = tempRoleMap.replaceAll("\\[", "").replaceAll("\\]","");
    tempRoleMap = tempRoleMap.replaceAll(" ", "");
    tempRoleMap = tempRoleMap.substring(0,tempRoleMap.length()-1);
      
      // creates a for loop that will split the tempRoleMap string into individual strings, then it removes each of the individual strings or email adresses from the distribution list
      for (String tempStrng: tempRoleMap.split(",")) {
        setDistribution(tempStrng, Recipient.USER, Recipient.REMOVE, usersList); // Params: User Email, Type Of Recipient, ADD/REMOVE, listObject.
      }
	

  
  
    // ADD/REMOVE ROLE                
JsonArray rolesList = new JsonArray();
setDistribution("09 Contractor", Recipient.ROLE, Recipient.REMOVE, rolesList);
setDistribution("09 Design Build Contractor", Recipient.ROLE, Recipient.REMOVE, rolesList);
setDistribution("10 Sub Contractor", Recipient.ROLE, Recipient.REMOVE, rolesList);



  // Set other options of create discussion screen                           
//responseJSON.add(IGroovyConstant.DESCRIPTION, addAttributes("true", "false", "Description From Groovy .."));    
  responseJSON.add(IGroovyConstant.MARK_AS_PRIVATE, addAttributes("false", "false", "true"));

  JsonObject distributionList = new JsonObject();   

  distributionList.add(Recipient.USER, usersList);
distributionList.add(Recipient.ROLE, rolesList);   

  distributionList.addProperty(IGroovyConstant.CLEAR_DISTRIBUTION_LIST, "false");    // By setting this value to true, it will clear all the recipients from the distribution list in 'To' field.                         

  distributionList.addProperty(IGroovyConstant.ENABLE_DISTRIBUTION_LIST,"true");

  distributionList.addProperty(IGroovyConstant.APPLY_TO_EDIT_DRAFT,"true");



  responseJSON.add(IGroovyConstant.DISTRIBUTION_LIST, distributionList);                

  document.setResponseStatus(false);

  document.setDynamicObject(responseJSON.toString());

  return document;

}

// Do not change anything in below functions.

// FOR REMOVE

public void setDistribution(String data, String distLevel, String operation, JsonArray arrayList) {

String ojbect = "name";

if(Recipient.USER.equals(distLevel)) {

ojbect = "email";

}

JsonObject jsonObject = new JsonObject();

jsonObject.addProperty(ojbect, data);

jsonObject.addProperty("operation", operation);

arrayList.add(jsonObject);

}

// TO ADD

public void setDistribution(String data, String distLevel, String operation, int actionId, String dueDays, JsonArray arrayList) {

String ojbect = "name";

if(Recipient.USER.equals(distLevel)) {

ojbect = "email";

}

JsonObject jsonObject = new JsonObject();

jsonObject.addProperty(ojbect, data);

jsonObject.addProperty("operation", operation);

jsonObject.addProperty("actionId", String.valueOf(actionId));

jsonObject.addProperty("dueDays", dueDays);

arrayList.add(jsonObject);

}

// PREPARE JSON

public JsonElement addAttributes(String applyToEditDraft, String editable, String defaultValue) {

JsonElement jsonObject = new JsonObject();

jsonObject.addProperty(IGroovyConstant.EDITABLE, editable);   

jsonObject.addProperty(IGroovyConstant.VALUE, defaultValue);

jsonObject.addProperty(IGroovyConstant.APPLY_TO_EDIT_DRAFT, applyToEditDraft);

return jsonObject;

}
//written by irubio
