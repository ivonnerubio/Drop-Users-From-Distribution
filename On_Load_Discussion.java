def execute(){ 
  // Fetch current document details
  DocumentVO document = documentService.getDocumentDetails();  
  String tempRoleMap = "";

  //Prepare ADD/REMOVE Recipients JSON
  JsonObject responseJSON = new JsonObject();        

  // ADD/REMOVE USERS
  JsonArray usersList = new JsonArray();
  setDistribution(document.getPublisherEmail(), Recipient.USER, Recipient.REMOVE, usersList); // Params: User Email, Type Of Recipient, ADD/REMOVE, listObject.
  

  // Find users with the role and Add/Remove from the Distribution List
  String roleName = "09 Contractor,09 Design Build Contractor,10 Sub Contractor";
  
  // Create the Distribution List and add the roles name
  List<String> distributionRoleList = new ArrayList<String>();
  List<String> finalDistributionRoleList = new ArrayList<String>();
  if(roleName != null && !roleName.isEmpty() && roleName.contains(",")){
 	 distributionRoleList = roleName.split(",").toList()
  } 
  else{
	  distributionRoleList.add(roleName);
  }

  distributionRoleList.each {
	  String it-> it = it.trim();  
	  finalDistributionRoleList.add(it)		
  }


  // Create a distribution map and add the distribution list on it
  Map<String, List<String>> distributionMap = new HashMap();
  distributionMap.put(IGroovyConstant.DISTRIBUTION_ROLES, finalDistributionRoleList);

  // Get users email address with the role previously selected
  Map<String, Map<String, List<String>>> RecipientUsersEmailIds  = documentService.getUsersEmailIdByGroupOrRole(distributionMap);
  Map<String,List<String>> RolesEmailMap =  RecipientUsersEmailIds.get(IGroovyConstant.DISTRIBUTION_ROLES);

  // Creates a for loop that will go through the rolemap and each one of the values. Then it will get the list remove, convert to string and clean up the string to just have the role email addresses. 
  for(Map.Entry<String, List<String>> roleMap  :RolesEmailMap.entrySet()){
      tempRoleMap += roleMap.getValue().toString()+",";
  }
  tempRoleMap = tempRoleMap.replaceAll("\\[", "").replaceAll("\\]","");
  tempRoleMap = tempRoleMap.replaceAll(" ", "");
  tempRoleMap = tempRoleMap.substring(0,tempRoleMap.length()-1);
      
  // Creates a for loop that will split the tempRoleMap string into individual strings, then it removes each of the individual strings or email adresses from the distribution list
  for (String tempStrng: tempRoleMap.split(",")) {
	  setDistribution(tempStrng, Recipient.USER, Recipient.REMOVE, usersList); // Params: User Email, Type Of Recipient, ADD/REMOVE, listObject.
  }

  // ADD/REMOVE ROLE                
  JsonArray rolesList = new JsonArray();
  setDistribution("09 Contractor", Recipient.ROLE, Recipient.REMOVE, rolesList);
  setDistribution("09 Design Build Contractor", Recipient.ROLE, Recipient.REMOVE, rolesList);
  setDistribution("10 Sub Contractor", Recipient.ROLE, Recipient.REMOVE, rolesList);



  // Set other options of create discussion screen                           
  // ResponseJSON.add(IGroovyConstant.DESCRIPTION, addAttributes("true", "false", "Description From Groovy .."));    
  responseJSON.add(IGroovyConstant.MARK_AS_PRIVATE, addAttributes("false", "false", "true"));
  JsonObject distributionList = new JsonObject();   
  distributionList.add(Recipient.USER, usersList);
  distributionList.add(Recipient.ROLE, rolesList);   
  distributionList.addProperty(IGroovyConstant.CLEAR_DISTRIBUTION_LIST, "false");   
  
  // By setting this value to true, it will clear all the recipients from the distribution list in 'To' field.                         
  distributionList.addProperty(IGroovyConstant.ENABLE_DISTRIBUTION_LIST,"true");
  distributionList.addProperty(IGroovyConstant.APPLY_TO_EDIT_DRAFT,"true");

  responseJSON.add(IGroovyConstant.DISTRIBUTION_LIST, distributionList);                
  document.setResponseStatus(false);
  document.setDynamicObject(responseJSON.toString());
  return document;

}
