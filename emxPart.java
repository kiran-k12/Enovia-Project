import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.ArrayList;
import matrix.db.Attribute;
import matrix.db.BusinessObject;
import matrix.db.BusinessObjectWithSelectList;
import matrix.db.Context;
import matrix.db.JPO;
import matrix.db.Relationship;
import matrix.db.BusinessType;
import matrix.db.Policy;
import matrix.db.PolicyList;
import matrix.db.PolicyItr;
import matrix.util.Pattern;
import matrix.util.SelectList;
import matrix.util.StringList;
import com.matrixone.apps.common.CommonDocument;
import com.matrixone.apps.domain.DomainConstants;
import com.matrixone.apps.domain.DomainObject;
import com.matrixone.apps.domain.DomainRelationship;
import com.matrixone.apps.domain.util.ContextUtil;
import com.matrixone.apps.domain.util.EnoviaResourceBundle;
import com.matrixone.apps.domain.util.FormatUtil;
import com.matrixone.apps.domain.util.FrameworkException;
import com.matrixone.apps.domain.util.FrameworkProperties;
import com.matrixone.apps.domain.util.FrameworkUtil;
import com.matrixone.apps.domain.util.MapList;
import com.matrixone.apps.domain.util.MqlUtil;
import com.matrixone.apps.domain.util.PropertyUtil;
import com.matrixone.apps.domain.util.i18nNow;
import com.matrixone.apps.domain.util.mxType;
import com.matrixone.apps.engineering.EngineeringUtil;
import com.matrixone.apps.engineering.EngineeringConstants;
import com.matrixone.apps.engineering.Part;
import com.pg.v3.custom.pgV3Constants;
import com.matrixone.apps.engineering.RelToRelUtil;
import com.matrixone.apps.domain.util.DebugUtil;
import com.matrixone.apps.framework.ui.UINavigatorUtil;
import com.matrixone.apps.cpn.util.ECMUtil;
import com.matrixone.apps.framework.ui.UIUtil;
//JPO Compilation Fixing 2015XUPGRADE START
//import com.matrixone.apps.cpn.customcpn.CPNCommonConstants;
import com.matrixone.apps.cpn.CPNCommonConstants;
//JPO Compilation Fixing 2015XUPGRADE END
import com.matrixone.apps.cpn.util.BusinessUtil;
import com.matrixone.jdom.Element;

import java.util.List;
import java.util.Locale;

import com.dassault_systemes.enovia.bom.modeler.constants.BOMMgtConstants;
import com.dassault_systemes.enovia.bom.modeler.interfaces.input.IBOMFilterIngress;
import com.dassault_systemes.enovia.bom.modeler.interfaces.services.IBOMService;
import com.dassault_systemes.enovia.formulation.custom.enumeration.FormulationAttribute;
import com.dassault_systemes.enovia.formulation.custom.enumeration.FormulationRelationship;
import com.dassault_systemes.enovia.formulation.custom.enumeration.FormulationType;
import com.dassault_systemes.enovia.formulation.custom.util.FormulationUtil;
//P&G: PLM V2 - End
//Merged by 2013x upgrade team end -
//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - START
import com.matrixone.apps.framework.ui.UITableIndented;
import com.matrixone.apps.engineering.EBOMMarkup;
import com.matrixone.jsystem.util.StringUtils;
import com.matrixone.apps.engineering.PartFamily;
//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - END
//DSM(DS) - 2018x.1.1 - ALM 25859 - PRB0058825 - CA Blocked from Completion Due to FPP added as CUP Substitute - START
import com.dassault_systemes.enovia.enterprisechangemgt.util.ChangeUtil;
//DSM(DS) - 2018x.1.1 - ALM 25859 - PRB0058825 - CA Blocked from Completion Due to FPP added as CUP Substitute - END

/**
 * The <code>emxPart</code> class contains code for the "Part" business type.
 *
 * @version EC 9.5.JCI.0 - Copyright (c) 2002, MatrixOne, Inc.
 */
  public class ${CLASSNAME} extends ${CLASS:emxPartBase}
  {
	 private static final ${CLASS:pgDSOCommonUtils} UTILS = ${CLASS:pgDSOCommonUtils}.INSTANCE;
	 /*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - START*/	
	 /* Overridden from emxECPartBase */
	
	private static final String ATTRIBUTE_PART_MODE = PropertyUtil.getSchemaProperty("attribute_PartMode");
	
	private static final String SELECT_PART_MODE = "to[" + RELATIONSHIP_PART_REVISION + "].from.attribute[" + ATTRIBUTE_PART_MODE + "]";
	
	private static final String SELECT_RAISED_AGAINST_ECR_CURRENT = "to[" + RELATIONSHIP_RAISED_AGAINST_ECR + "].from[" + TYPE_ECR + "].current";
	
	private static final String SELECT_RAISED_AGAINST_ECR = "to[" + RELATIONSHIP_RAISED_AGAINST_ECR + "].from[" + TYPE_ECR + "].name";
	
	//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - START
	private static final String MARKUP_ADD = "add";
    private static final String MARKUP_NEW = "new";
    private static final String MARKUP_CUT = "cut";
	private static final String ATTRIBUTE_DERIVED_CONTEXT = PropertyUtil.getSchemaProperty("attribute_DerivedContext");
    private static final String SELECT_FROM_DERIVED_IDS = "from[" + RELATIONSHIP_DERIVED + "]." + DomainConstants.SELECT_TO_ID;
	//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - END
	
	/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - End*/
	
	//JPO Compilation Fixing 2015XUPGRADE START
	//private static final String str_TYPE_PRODUCT_DATA = "";
	//private static final String str_SELECT_ATTRIBUTE_STAGE = "";
	//JPO Compilation Fixing 2015XUPGRADE END
	
	public static final String RELATIONSHIP_EBOM_SUBSTITUTE = ${CLASS:pgDSOConstants}.RELATIONSHIP_EBOM_SUBSTITUTE;
	
	public String TYPE_PGSIGNUPFORM = ${CLASS:pgDSOConstants}.TYPE_PGSIGNUPFORM;
	public String TYPE_PART = ${CLASS:pgDSOConstants}.TYPE_PART;
	public String TYPE_TECHNICAL_SPECIFICATION = ${CLASS:pgDSOConstants}.TYPE_TECHNICAL_SPECIFICATION;
	public String TYPE_PRODUCT_DATA = TYPE_PGSIGNUPFORM +","+ TYPE_PART +","+ TYPE_TECHNICAL_SPECIFICATION;
	//Modified for 2018x Upgrade - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
	private static final String SELECT_ATTRIBUTE_STAGE = ${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE;
	/**
	 * Constructor.
	 *
	 * @param context the eMatrix <code>Context</code> object.
	 * @param args holds no arguments.
	 * @throws Exception if the operation fails.
	 * @since EC 9.5.JCI.0.
	 */
      public ${CLASSNAME} (Context context, String[] args)throws Exception
      {
          super(context, args);
      }
	public MapList getEBOMsWithRelSelectables (Context context, String[] args) throws Exception
	{
		HashMap paramMap = (HashMap)JPO.unpackArgs(args);
		int nExpandLevel = 0;
		String sExpandLevels = (String)paramMap.get("emxExpandFilter");
		if(sExpandLevels == null || sExpandLevels.equals("null") || sExpandLevels.equals("")) {
			sExpandLevels = (String)paramMap.get("ExpandFilter");
		}
		StringList selectStmts = new StringList(1);
		StringBuffer sObjWhereCond = new StringBuffer();
		if(sExpandLevels==null || sExpandLevels.length()==0)
		{
			nExpandLevel = 1;
		}
		else
		{
			if("All".equalsIgnoreCase(sExpandLevels))
				nExpandLevel = 0;
			else if (sExpandLevels != null && sExpandLevels.equalsIgnoreCase("EndItem"))
			{
				nExpandLevel=0;
				if(sObjWhereCond.length()>0){
					sObjWhereCond.append(" && ");
				}
				sObjWhereCond.append("(" + EngineeringConstants.SELECT_END_ITEM + " == '"
						+ EngineeringConstants.STR_NO + "')");
				selectStmts.addElement("from["+EngineeringConstants.RELATIONSHIP_EBOM+"].to.attribute[End Item]");
				selectStmts.addElement("from["+EngineeringConstants.RELATIONSHIP_EBOM+"].id");
			}
			else
				nExpandLevel = Integer.parseInt(sExpandLevels);
		}
		String partId = (String) paramMap.get("objectId");
		//Added for PUE ECC Reports - R210
		String strObjectId1=(String)paramMap.get("strObjectId1");
		String strObjectId2=(String)paramMap.get("strObjectId2");
		String strEffVal ="";
		if ( strObjectId1 != null &&  strObjectId1.equals(partId)) {
			strEffVal = "Eff Param 1";
		} else {
			strEffVal = "Eff Param 2";
		}
		String strSide = (String) paramMap.get("side");
		String strConsolidatedReport=(String)paramMap.get("isConsolidatedReport");
		MapList ebomList = new MapList();
		// reportType can be either BOM or AVL. Depending on this value Location Name is set.
		String reportType ="";
		// location variable holds the value of Location Name
		String location = "";
		//Added for IR-021267
		/*
		 * StringList to store the selects on the Domain Object
		 */
		/*
		 * StringList to store the selects on the relationship
		 */
		StringList selectRelStmts = new StringList(6);
		/*
		 * String buffer to prepare where condition with  End Item value
		 */
		/*
		 * stores the location ID
		 */
		String locationId = null;
		/*
		 * Maplist holds the data from the getCorporateMEPData method
		 */
		MapList tempList = null;
		// retrieve the selected reportType from the paramMap
		reportType = (String) paramMap.get("reportType");
		// retrieve the selected location by the user
		location = (String) paramMap.get("location");
		// Object Where Clause added for Revision Filter
		String complete = PropertyUtil.getSchemaProperty(context,"policy", DomainConstants.POLICY_DEVELOPMENT_PART, "state_Complete");
		String selectedFilterValue = (String) paramMap.get("ENCBOMRevisionCustomFilter");
		if (selectedFilterValue == null)
		{
			if (strSide != null)
			{
				selectedFilterValue = (String) paramMap.get(strSide+"RevOption");
			}
			if (selectedFilterValue == null)
			{
				selectedFilterValue = "As Stored";
			}
		}
		//Commented for 098364
		//    if(selectedFilterValue.equals("Latest Complete")) {
		//       sObjWhereCond.append("((current == " +complete+") && (revision == last))||((current == "+ complete+") && (next.current != "+complete+"))");
		//    }
		// To display AVL data for the first time with default Host Company of the user.
		try {
			Part partObj = new Part(partId);
			selectStmts.addElement(DomainConstants.SELECT_ID);
			selectStmts.addElement(DomainConstants.SELECT_TYPE);
			selectStmts.addElement(DomainConstants.SELECT_NAME);
			selectStmts.addElement(DomainConstants.SELECT_REVISION);
			selectStmts.addElement(DomainConstants.SELECT_DESCRIPTION);
			//Added for hasChildren
			selectStmts.addElement("from["+DomainConstants.RELATIONSHIP_EBOM+"]");
			// Added for MCC EC Interoperability Feature
			String strAttrEnableCompliance  =PropertyUtil.getSchemaProperty(context,"attribute_EnableCompliance");
			selectStmts.addElement("attribute["+strAttrEnableCompliance+"]");
			//end
			//Merged by 2013x upgrade team start -
			//P&G: Modification starts by PLMv2-PDF view BOM
			selectRelStmts.addElement("attribute[Comment]");
			selectRelStmts.addElement("attribute[pgIncludeInCOSAnalysis]");
			selectRelStmts.addElement("attribute[pgPositionIndicator]");
			selectRelStmts.addElement("attribute[pgMinQuantity]");
			selectRelStmts.addElement("attribute[pgQuantity]");
			selectRelStmts.addElement("attribute[pgMaxQuantity]");
			selectRelStmts.addElement("attribute[pgQuantityUnitOfMeasure]");
			selectRelStmts.addElement("attribute[pgChange]");
			selectRelStmts.addElement("attribute[pgMaterialFunction]");
			selectStmts.addElement("attribute[pgBatchUnitSize]");
			selectStmts.addElement("attribute[Title]");
			selectStmts.addElement("attribute[pgCSSType]");
			selectStmts.addElement("attribute[pgAssemblyType]");
			selectStmts.addElement("attribute[pgPackingLevel]");
			selectStmts.addElement("attribute[pgFinishedProductCode]");
			selectStmts.addElement("attribute[Comment]");
			selectStmts.addElement("attribute[pgPackagingSize]");
			selectRelStmts.addElement("from.name");
			selectRelStmts.addElement("from.description");
			//P&G: Modification End
			//Merged by 2013x upgrade team end -
			selectRelStmts.addElement(DomainConstants.SELECT_RELATIONSHIP_ID);
			selectRelStmts.addElement(SELECT_ATTRIBUTE_REFERENCE_DESIGNATOR);
			selectRelStmts.addElement(SELECT_ATTRIBUTE_QUANTITY);
			selectRelStmts.addElement(SELECT_ATTRIBUTE_FIND_NUMBER);
			selectRelStmts.addElement(SELECT_ATTRIBUTE_COMPONENT_LOCATION);
			selectRelStmts.addElement(SELECT_ATTRIBUTE_USAGE);
			//Merged by 2013x upgrade team start -
			//P&G: Added by PLM V2 for including some Rel Select related to EBOM Relationship :Start
			final String SELECT_SUBSTITUTE_PARTS="frommid[EBOM Substitute]";
			selectRelStmts.addElement(SELECT_SUBSTITUTE_PARTS);
			selectStmts.addElement(DomainConstants.SELECT_ATTRIBUTE_UNITOFMEASURE);
			//P&G: Added by PLM V2 for including some Rel Select related to EBOM Relationship :End
			//Merged by 2013x upgrade team end -
			// int level=1;
			int level = nExpandLevel;
			ebomList = partObj.getRelatedObjects(context,
					DomainConstants.RELATIONSHIP_EBOM,  // relationship pattern
					DomainConstants.TYPE_PART,                  // object pattern
					selectStmts,                 // object selects
					selectRelStmts,              // relationship selects
					false,                        // to direction
					true,                       // from direction
					(short)level,                    // recursion level
					sObjWhereCond.toString(),     // object where clause
					null);                       // relationship where clause
			//      IR023752    start
			if("EndItem".equalsIgnoreCase(sExpandLevels) )
			{
				ebomList = getEndItemMapList(context, ebomList, partObj, selectStmts, selectRelStmts);
			}
			//Merged by 2013x upgrade team start -
			//P&G: Added by PLM V2 for expand baseformula in BOM Table :Start
			MapList pgPhaseList  = new MapList();
			Map pgPhaseMap = new HashMap();
			for (int i=0; i<ebomList.size(); i++) {
				Map ebomMap = (Map) ebomList.get(i);
				String ebId = (String)ebomMap.get(DomainConstants.SELECT_ID);
				String eType = (String)ebomMap.get(DomainConstants.SELECT_TYPE);
				String elevel = (String)ebomMap.get("level");
				if(eType.equalsIgnoreCase("pgBaseFormula")){
					//check if base formula was already expanded or not by checking the level of immediate next obj
					if(ebomList.size()>(i+1)){
						Map ebomNextMap = (Map) ebomList.get(i+1);
						String eNextlevel = (String)ebomNextMap.get("level");
						if(Integer.parseInt(elevel)<Integer.parseInt(eNextlevel)){
							continue;
						}
					}
					DomainObject pgPhase = DomainObject.newInstance(context,ebId);
					pgPhaseList = pgPhase.getRelatedObjects(context,
							DomainConstants.RELATIONSHIP_EBOM,  // relationship pattern
							DomainConstants.TYPE_PART,                  // object pattern
							selectStmts,                 // object selects
							selectRelStmts,              // relationship selects
							false,                        // to direction
							true,                       // from direction
							(short)1,                    // recursion level
							"",     // object where clause
							null,0);
					for (Iterator phsItr = pgPhaseList.iterator(); phsItr.hasNext();) {
						Map ebom_pgPhase = (Map) phsItr.next();
						//make the levels as 3 and add them to appropriate
						int phsLevel = Integer.parseInt(elevel) + 1;
						ebom_pgPhase.put("level",""+phsLevel);
					}
					pgPhaseMap.put(i, pgPhaseList);					
				}
			}
			Set phsMapKeySet = pgPhaseMap.keySet();
			for (Iterator iterator = phsMapKeySet.iterator(); iterator.hasNext();) {
				int keyIndex = (Integer) iterator.next();
				ebomList.addAll(keyIndex+1, (MapList)pgPhaseMap.get(keyIndex));
			}
			//P&G: Added by PLM V2 for expand baseformula in BOM Table :End	
			//Merged by 2013x upgrade team end -
			//IR023752 end
			if(strConsolidatedReport != null && strConsolidatedReport.equalsIgnoreCase("Yes"))
			{
				MapList newBOMList = new MapList();
				getFlattenedMapList(ebomList,newBOMList);
				ebomList = newBOMList;
			}
			// Below code get the last revision of a domain object even if it is not connected to EBOM
			int ebomSize = ebomList.size();
			//  -Modified for the fix IR-013085
			//if(ebomList!=null && ebomSize>0 && selectedFilterValue.equals("Latest")) {
			if((ebomList!=null && ebomSize>0 && selectedFilterValue.equals("Latest"))||(selectedFilterValue.equals("Latest Complete"))) {
				Iterator itr = ebomList.iterator();
				MapList LRev = new MapList();
				String objID = "";
				//Iterate through the maplist and add those parts that are latest but not connected
				while(itr.hasNext()) {
					Map newMap = (Map)itr.next();
					String ObjectId = (String)newMap.get("id");
					String oldRev = (String)newMap.get("revision");
					DomainObject domObj = DomainObject.newInstance(context,ObjectId);
					// get the last revision of the object
					BusinessObject bo = domObj.getLastRevision(context);
					bo.open(context);
					objID = bo.getObjectId();
					String newRev = bo.getRevision();
					//Modifed for the IR-013085
					bo.close(context);
					if (selectedFilterValue.equals("Latest"))
					{
						if(oldRev != newRev || !oldRev.equals(newRev))
						{
							newMap.put("id",objID);
						}
					}
					//Added for the IR-013085
					else if(selectedFilterValue.equals("Latest Complete"))
					{
						DomainObject domObjLatest = DomainObject.newInstance(context,objID);
						String currSta = domObjLatest.getInfo(context,DomainConstants.SELECT_CURRENT);
						//Added for the IR-026773
						if (oldRev.equals(newRev))
						{
							if (!complete.equals(currSta))
								continue;
							newMap.put("id",objID);
						}//IR-026773 ends
						else
						{
							while (!currSta.equalsIgnoreCase(complete)&&!currSta.equals(complete))
							{
								BusinessObject boObj = domObjLatest.getPreviousRevision(context);
								boObj.open(context);
								objID = boObj.getObjectId();
								currSta = (String)(DomainObject.newInstance(context,objID).getInfo(context,DomainConstants.SELECT_CURRENT));
								boObj.close(context);
							}
							newMap.put("id",objID);
						}
					}// IR-013085 ends
					//Add new map to the HashMap
					LRev.add (newMap);
				}
				ebomList.clear();
				ebomList.addAll(LRev);
			}
			if (location!=null && ("").equals(location) && reportType!=null && reportType.equals("AVL"))
			{
				// retrieve the Person object
				//           com.matrixone.apps.common.Person person = (com.matrixone.apps.common.Person)DomainObject.newInstance(context, DomainConstants.TYPE_PERSON);
				// retrieve the Host Company attached to the User.
				location =com.matrixone.apps.common.Person.getPerson(context).getCompany(context).getObjectId(context);
			}
			if (location!=null && reportType!=null && reportType.equals("AVL"))
			{
				tempList = new MapList();
				//             com.matrixone.apps.common.Person person = (com.matrixone.apps.common.Person)DomainObject.newInstance(context, DomainConstants.TYPE_PERSON);
				locationId = com.matrixone.apps.common.Person.getPerson(context).getCompany(context).getObjectId(context);
				if (locationId.equals(location))
				{
					// In case of Host Company
					tempList = partObj.getCorporateMEPData(context, ebomList, locationId, true, partId);
				}
				else {
					// In case of selected location and All locations
					tempList = partObj.getCorporateMEPData(context, ebomList, location, false, partId);
				}
				ebomList.clear();
				ebomList.addAll(tempList);
			}
			// fix for bug 311050
			//check the parent obj state
			boolean allowChanges = true;
			StringList strList  = new StringList(2);
			strList.add(SELECT_CURRENT);
			strList.add("policy");
			Map map = partObj.getInfo(context,strList);
			String objState = (String)map.get(SELECT_CURRENT);
			String objPolicy = (String)map.get("policy");
			String propAllowLevel = FrameworkProperties.getProperty(context, "emxEngineeringCentral.Part.RestrictPartModification");
			StringList propAllowLevelList = new StringList();
			if(propAllowLevel != null || !("".equals(propAllowLevel)) || !("null".equals(propAllowLevel)))
			{
				StringTokenizer stateTok = new StringTokenizer(propAllowLevel, ",");
				while (stateTok.hasMoreTokens())
				{
					String tok = (String)stateTok.nextToken();
					propAllowLevelList.add(FrameworkUtil.lookupStateName(context, objPolicy, tok));
				}
			}
			allowChanges = (!propAllowLevelList.contains(objState));
			//set row editable option
			Iterator itr = ebomList.iterator();
			MapList tList = new MapList();
			while(itr.hasNext())
			{
				Map newMap = (Map)itr.next();
				if(allowChanges)
				{
					newMap.put("RowEditable", "show");
				}else
				{
					newMap.put("RowEditable", "readonly");
				}
				tList.add (newMap);
			}
			ebomList.clear();
			ebomList.addAll(tList);
			//end of fix for bug 311050
			// fix for bug 311050
			//check the parent obj state
			// boolean allowChanges = true;
			// StringList strList  = new StringList(2);
			strList.add(SELECT_CURRENT);
			strList.add("policy");
			// Map map = partObj.getInfo(context,strList);
			// String objState = (String)map.get(SELECT_CURRENT);
			//  String objPolicy = (String)map.get("policy");
			// String propAllowLevel = FrameworkProperties.getProperty("emxEngineeringCentral.Part.RestrictPartModification");
			// StringList propAllowLevelList = new StringList();
			if(propAllowLevel != null || !("".equals(propAllowLevel)) || !("null".equals(propAllowLevel)))
			{
				StringTokenizer stateTok = new StringTokenizer(propAllowLevel, ",");
				while (stateTok.hasMoreTokens())
				{
					String tok = (String)stateTok.nextToken();
					propAllowLevelList.add(FrameworkUtil.lookupStateName(context, objPolicy, tok));
				}
			}
			allowChanges = (!propAllowLevelList.contains(objState));
			//set row editable option
			//Iterator itr = ebomList.iterator();
			//  MapList tList = new MapList();
			while(itr.hasNext())
			{
				Map newMap = (Map)itr.next();
				if(allowChanges)
				{
					newMap.put("RowEditable", "show");
				}else
				{
					newMap.put("RowEditable", "readonly");
				}
				tList.add (newMap);
			}
			ebomList.clear();
			ebomList.addAll(tList);
			//end of fix for bug 311050
			//Merged by 2013x upgrade team start -
			//P&G: Added by PLM V2 To sort BOM structure based on Name for pgPhase and Find Number for childs :Start
			MapList mlEBOMDataToSort = new MapList(10);
			Iterator itrEBOMData = ebomList.iterator();
			String strLastLevel = "1";
			MapList mlLastMapList= mlEBOMDataToSort;
			MapList mlReferenceList= new MapList(5);
			while(itrEBOMData.hasNext()) {
				Map mPartEBOM = (Map)itrEBOMData.next();
				String strLevel = (String)mPartEBOM.get("level");
				int intLastLevel = Integer.parseInt(strLastLevel);
				int intLevel = Integer.parseInt(strLevel);
				strLastLevel = strLevel;
				if(intLastLevel==intLevel) {
					mlLastMapList.add(mPartEBOM);
				}
				if(intLevel>intLastLevel) {
					Map mLastMap = (Map)mlLastMapList.get(mlLastMapList.size()-1);
					MapList mlChildMapList = new MapList(3);
					mlChildMapList.add(mPartEBOM);
					mLastMap.put("ChildStructure",mlChildMapList);
					Map mReferenceMap = new HashMap();
					mReferenceMap.put("Reference",mlLastMapList);
					mlReferenceList.add(mReferenceMap);
					mlLastMapList = mlChildMapList;
				} else if (intLevel<intLastLevel){
					MapList mlTemp = new MapList();
					for(int i=0;i<intLevel;i++)
						mlTemp.add((Map)mlReferenceList.get(i));
					mlReferenceList = mlTemp;	
					Map mReferenceMap = (Map)mlReferenceList.get(mlReferenceList.size()-1);
					mlLastMapList = (MapList)mReferenceMap.get("Reference");
					mlLastMapList.add(mPartEBOM);
					mlReferenceList.remove(mlReferenceList.size()-1);
				}
			}
			MapList mlSortedList = new MapList(10);
			String strSortLevel = "2";
			if(mlEBOMDataToSort.size()>0) {
				Map mFirstMap = (Map)mlEBOMDataToSort.get(0);
				String strType = (String)mFirstMap.get("type");
				if(strType.equals("pgPhase"))
					strSortLevel = "1";
			}
			sortBOMStructure(mlEBOMDataToSort,mlSortedList,strSortLevel);
			ebomList=mlSortedList;
			//P&G: Added by PLM V2 To sort BOM structure based on Name for pgPhase and Find Number for childs :End
			//P&G: Added by PLM V2 to implement security on each object in maplist to be returned. If user does not have access on object, it should not be expanded further - Start
			boolean isChildExpandable = true;
			int intLevelBasic = 1;
			for(int i=0;i<ebomList.size();i++)
			{
				Map mapTest = (Map)ebomList.get(i);
				String strType = (String)mapTest.get("type");
				String strLevel = (String)mapTest.get("level");
				int intLevel = Integer.parseInt(strLevel);
				String strName = (String)mapTest.get("name");
				String strID = (String)mapTest.get("id");
				DomainObject dObj = new DomainObject(strID);
				if(!isChildExpandable && (intLevel >  intLevelBasic))
				{
					ebomList.remove(i);
					i--;
				}
				else
				{
					isChildExpandable = true;
					intLevelBasic = 1;
				}
				//DSM (DS) 2015x.1 - Commented below code as Custom Security is not in use - START
				/*if(!"pgPhase".equalsIgnoreCase(strType) && isChildExpandable)
				{
					String arrArgs[] = {strID};
					
					${CLASS:pgSecurityAccess} pgIPMSecAccess = new ${CLASS:pgSecurityAccess}(context, args);
					boolean bIsAccessAllowed = (boolean)pgIPMSecAccess.hasAccess(context,arrArgs);
					
					if(bIsAccessAllowed)
					{
						isChildExpandable = false;
						intLevelBasic = intLevel;
					}
				}*/
				//DSM (DS) 2015x.1 - Commented below code as Custom Security is not in use - END
			}
			//P&G: Added by PLM V2 to implement security on each object in maplist to be returned. If user does not have access on object, it should not be expanded further - Ends
			//Merged by 2013x upgrade team end -
		}
		catch (FrameworkException Ex) {
			throw Ex;
		}
		return ebomList;
	}
	//Merged by 2013x upgrade team start -
	//P&G: Added below methods by PLM V2 for EBOM table update :Start
	/**
	 * This method sorts the BOM Structure base on Name for pgPhase and based on Find Number for Child objects
	 * @param mlToBeSort - Maplist to be sorted
	 * @param mlSortedList - Sorted Maplist
	 * @param strLevel - Level of BOM
	 * @return nothing.
	 * @throws Exception if the operation fails.
	 */
	public void sortBOMStructure(MapList mlToBeSort, MapList mlSortedList, String strLevel) throws Exception {
		if(strLevel.equals("1")) {
			mlToBeSort.addSortKey("name", "ascending", "String");
		} else {
			mlToBeSort.addSortKey("attribute[Find Number]", "ascending", "integer");
		}
		mlToBeSort.sort();
		Iterator itrMapList = mlToBeSort.iterator();
		while (itrMapList.hasNext()) {
			Map mEBOM = (Map)itrMapList.next();
			mlSortedList.add(mEBOM);
			MapList mlChildList = (MapList)mEBOM.get("ChildStructure");
			if(mlChildList!=null) {
				mEBOM.remove("ChildStructure");
				sortBOMStructure(mlChildList,mlSortedList,"2");
			}
		}
	}
	/**
	 * This Method will return the flag for if the corressponding part are having the substitute parts or not
	 * in CPNENCEBOMIndentedSummary table
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getColumnSubstitutePartsFlagData(Context context,String[] args) throws Exception
	{
		HashMap paramMap = (HashMap)JPO.unpackArgs(args);
		Map paramList = (Map) paramMap.get("paramList");
		String parentId = (String) paramList.get("objectId");
		MapList objectList = (MapList) paramMap.get("objectList");
		Vector<String> vcSubstituteFlag = new Vector<String>(objectList.size());
		String strReportFormat = (String)paramList.get("reportFormat");
		String strTypeFormulaCard = PropertyUtil.getSchemaProperty("type_pgFormulatedProduct");
		String strTypeFinishedProduct = PropertyUtil.getSchemaProperty("type_pgFinishedProduct");
		String strTypeMasterFinishedProduct = PropertyUtil.getSchemaProperty("type_pgMasterFinishedProduct");
		String strTypeBaseFormula = PropertyUtil.getSchemaProperty("type_pgBaseFormula");
		String strTypepgPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strAttrTitle = PropertyUtil.getSchemaProperty("attribute_Title");
		//DSO : Changes to invoke Table "pgIPMFPSubstitutionTable" for DSO Types  : START
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		String strIncludeTypes = FrameworkProperties.getProperty(context, "emxCPN.ProductDataTemplate.type_ProductData.TypeInclusionList");
		StringList slTypes =  new StringList();
		try{
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		ContextUtil.pushContext(context);
		isContextPushed = true;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		if(UIUtil.isNotNullAndNotEmpty(strIncludeTypes)){
			StringList slSymIncludeTypes = FrameworkUtil.split(strIncludeTypes,",");
			for(Object strtType : slSymIncludeTypes){
				 slTypes.add((String)PropertyUtil.getSchemaProperty(context, strtType.toString()));
			 }
		}
		//DSO : Changes to invoke Table "pgIPMFPSubstitutionTable" for DSO Types  : END
		DomainObject dObjParentObject = new DomainObject(parentId);
		// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		//if(FrameworkUtil.hasAccess(context, dObjParentObject , "read")!=false){
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
		String strObjectType = dObjParentObject.getInfo(context,DomainConstants.SELECT_TYPE);
		String strSubHeaderPart = "";
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			Map object = (Map) iterator.next();
			String connectionId=(String)object.get("id[connection]");
		
			String objectId = (String) object.get("id");
			String objectName = (String) object.get(DomainConstants.SELECT_NAME);
			String objectDesc = (String) object.get(DomainConstants.SELECT_DESCRIPTION);
			String strTableName = "pgIPMEBOMPartsSubstituteTable";
			String strSortColumnName="SubsCombNo";
			String strSubHeader = "";
			DomainObject dObjSubstituted = new DomainObject(objectId);
			// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
			// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
			//if(FrameworkUtil.hasAccess(context, dObjSubstituted , "read")!=false){
			// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
			String strIndividualObjectType = dObjSubstituted.getInfo(context,DomainConstants.SELECT_TYPE);
			String strLevel = (String) object.get("level");
			int intLevel = Integer.parseInt(strLevel);
			String strAttrTitleValue = dObjSubstituted.getInfo(context, "attribute["+strAttrTitle+"]");
			if(strTypeFormulaCard.equalsIgnoreCase(strObjectType))
			{
				strTableName = "pgIPMFormulaCardSubstitutionTable";
				strSubHeader = "Used in Formulation";				
			}
			//P&G: Added condition for pgPackingSubassembly for V4
			else if(strTypeFinishedProduct.equalsIgnoreCase(strObjectType) || strTypeMasterFinishedProduct.equalsIgnoreCase(strObjectType) || pgV3Constants.TYPE_PGPACKINGSUBASSEMBLY.equalsIgnoreCase(strObjectType))
			{
				strTableName = "pgIPMFPSubstitutionTable";
				strSubHeader = "Used in Packing Table";		
			}
			else if(strTypeBaseFormula.equalsIgnoreCase(strObjectType))
			{
				strTableName = "pgIPMBaseFormulaSubstitutionTable";
			}
			//DSO : Added Else-If condition to invoke Table "pgIPMFPSubstitutionTable" for DSO Types  : START
			else if(slTypes.contains(strObjectType)){
				strTableName = PropertyUtil.getSchemaProperty(context, "table_pgIPMFPSubstitutionTable");
				strSubHeader = "Used in Packing Table";	
			}
			//DSO : Added Else-If condition to invoke Table "pgIPMFPSubstitutionTable" for DSO Types  : END
			StringList slRelSelect= new StringList();
			final String SELECT_SUBSTITUTE="frommid[EBOM Substitute]";
			slRelSelect.add(SELECT_SUBSTITUTE);
			slRelSelect.add(DomainRelationship.SELECT_ID);
			slRelSelect.add("from");
			slRelSelect.add("from.description");
			if(null != connectionId && !"".equalsIgnoreCase(connectionId) )
			{
				//DSM2015x.1 Fix for HiR Formula Card Expand All - Start
				ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
				MapList mlList=DomainRelationship.getInfo(context, new String[]{connectionId}, slRelSelect);
				ContextUtil.popContext(context);
				//DSM2015x.1 Fix for HiR Formula Card Expand All - End
				for(int i=0;i<mlList.size();i++)
				{
					Map obj1=(Map)mlList.get(i);
					String strSubstitute=(String)obj1.get(SELECT_SUBSTITUTE);
					String strRelId= (String)obj1.get(DomainRelationship.SELECT_ID);
					String strpgPhaseName = (String)obj1.get("from");
					//String strpgPhaseDesc = (String)obj1.get("from.description");				
					//DSO 2013X.4 - Added for Special CHaracters in Description - Start
					String strpgPhaseDesc = "";	
					//DSO 2013X.4 - Added for Special CHaracters in Description - End
					String strObjName = (String) object.get("name");
						
					//DSO : Handling NULL value for Substituted Object Name in case of DSO Types : START
					if(UIUtil.isNullOrEmpty(strObjName) && UIUtil.isNotNullAndNotEmpty(objectId)){
						strObjName = dObjSubstituted.getInfo(context, DomainConstants.SELECT_NAME);
					}
					//DSO : Handling NULL value for Substituted Object Name in case of DSO Types : END
					// Modified for defect 4588 - Special characters are not displayed properly in Substitution Table
					strAttrTitleValue = strAttrTitleValue.replaceAll("%","%2525");
					strAttrTitleValue = strAttrTitleValue.replaceAll("&","%2526");
					strAttrTitleValue = strAttrTitleValue.replaceAll("#","%2523");
					strAttrTitleValue = strAttrTitleValue.replaceAll("'","%2527");
					CharSequence csDollarTarget = "%2524";
					CharSequence csDollarSource = "$";
					strAttrTitleValue = strAttrTitleValue.replace(csDollarSource,csDollarTarget);
					CharSequence csPlusTarget = "%252B";
					CharSequence csPlusSource = "+";
					strAttrTitleValue = strAttrTitleValue.replace(csPlusSource,csPlusTarget);
					CharSequence csTarget = "%255C";
					CharSequence csSource = "\\";
					strAttrTitleValue = strAttrTitleValue.replace(csSource,csTarget);
					strAttrTitleValue = strAttrTitleValue.replaceAll("\"","%2522");
					strAttrTitleValue = strAttrTitleValue.replaceAll("<","%253C");
					System.out.println("\n strAttrTitleValue "+strAttrTitleValue);
					// Modification for defect 4588 ends
					//P&G: Added condition for pgPackingSubassembly for V4
					if(strTypeFormulaCard.equalsIgnoreCase(strObjectType) || strTypeFinishedProduct.equalsIgnoreCase(strObjectType) || strTypeMasterFinishedProduct.equalsIgnoreCase(strObjectType) || pgV3Constants.TYPE_PGPACKINGSUBASSEMBLY.equalsIgnoreCase(strObjectType))
					{
						// Modified for defect 4588 - Special characters are not displayed properly in Substitution Table
						strpgPhaseDesc = strpgPhaseDesc.replaceAll("%","%2525");
						strpgPhaseDesc = strpgPhaseDesc.replaceAll("&","%2526");
						strpgPhaseDesc = strpgPhaseDesc.replaceAll("#","%2523");
						strpgPhaseDesc = strpgPhaseDesc.replaceAll("'","%2527");
						strpgPhaseDesc = strpgPhaseDesc.replace(csDollarSource,csDollarTarget);
						strpgPhaseDesc = strpgPhaseDesc.replace(csPlusSource,csPlusTarget);
						strpgPhaseDesc = strpgPhaseDesc.replace(csSource,csTarget);
						strpgPhaseDesc = strpgPhaseDesc.replaceAll("\"","%2522");
						strpgPhaseDesc = strpgPhaseDesc.replaceAll("<","%253C");
						// Modification for defect 4588 ends
						if(strTypeFormulaCard.equalsIgnoreCase(strObjectType)){					
							strSubHeaderPart = "FIL Description";
						}
						else{
							strSubHeaderPart = "PCT Description";
						}
					}
					if(strIndividualObjectType.equalsIgnoreCase(strTypepgPhase) && intLevel == 1){
						vcSubstituteFlag.add("");
					}
					else if("True".equalsIgnoreCase(strSubstitute))
					{
						StringBuffer stringBuffer = new StringBuffer();
						//DSO : Added to display the title correctly on Substitute table page - START
						StringBuffer sbTitle = new StringBuffer();
						//DSO : Added to display the title correctly on Substitute table page - END
						if(strReportFormat != null && strReportFormat.length()>0)
						{
							stringBuffer.append("Yes");
						}
						else{
							//Merged by 2013x upgrade team start -
							//P&G: Modification starts by PLM v2 -Modify for Sorting :Start		
							//stringBuffer.append("<a href=\"javascript:emxTableColumnLinkClick('../common/emxTable.jsp?suiteKey=EngineeringCentral&amp;program=emxPart:getTableEBOMPartsSubstituteList&amp;table="+strTableName+"&amp;HelpMarker=emxhelppartsubstitute&amp;sortColumnName="+strSortColumnName+"&amp;PrinterFriendly=true&amp;objectId="+objectId+"&amp;parentId="+parentId+"&amp;fromEBOMSubstitute=True&amp;relId=");
							//P&G: Modified for defect No 441: Start
							//stringBuffer.append("<a href=\"javascript:emxTableColumnLinkClick('../common/emxTable.jsp?suiteKey=EngineeringCentral&amp;program=emxPart:getTableEBOMPartsSubstituteList&amp;table="+strTableName+"&amp;HelpMarker=emxhelppartsubstitute&amp;PrinterFriendly=true&amp;objectId="+objectId+"&amp;parentId="+parentId+"&amp;fromEBOMSubstitute=True&amp;relId=");
							//DSO 2013x.4- Removed editLink START
							//IPM V2/V3 2013x.5 Modification for Defect ID 3466 - Starts
							/*Added new Parameter checkAccess=false, so that when the user clicks on the hyper-link(i.e. "Yes") the user does not get the
							alert saying : You are not authorized to view this Product Data
							but instead the user is allowed to view the Substitution Column Data, and if the user clicks on the GCAS thereafter on the Pop-up page the security is checked for that action
							*/
							//stringBuffer.append("<a href=\"javascript:emxTableColumnLinkClick('../common/pgIPMTable.jsp?suiteKey=EngineeringCentral&amp;program=emxPart:getTableEBOMPartsSubstituteList&amp;table="+strTableName+"&amp;HelpMarker=emxhelppartsubstitute&amp;PrinterFriendly=true&amp;objectId="+objectId+"&amp;parentId="+parentId+"&amp;fromEBOMSubstitute=True&amp;checkAccess=false&amp;relId=");
							//Modified by DSM(Sogeti)-2015x.4.1 Feb downtime for Defect Id-15145 -- STARTS
							//stringBuffer.append("<a href=\"javascript:emxTableColumnLinkClick('../common/pgIPMTable.jsp?suiteKey=EngineeringCentral&amp;program=emxPart:getTableEBOMPartsSubstituteList&amp;table="+strTableName+"&amp;HelpMarker=emxhelppartsubstitute&amp;PrinterFriendly=true&amp;objectId="+objectId+"&amp;parentId="+parentId+"&amp;fromEBOMSubstitute=True&amp;checkAccess=false&amp;tableType=new&amp;relId=");
							stringBuffer.append("<a href=\"javascript:emxTableColumnLinkClick('../common/pgIPMTable.jsp?suiteKey=EngineeringCentral&amp;program=emxPart:getTableEBOMPartsSubstituteList&amp;table="+strTableName+"&amp;HelpMarker=emxhelppartsubstitute&amp;PrinterFriendly=true&amp;objectId="+objectId+"&amp;parentId="+parentId+"&amp;fromEBOMSubstitute=True&amp;checkAccess=false&amp;relId=");
							
							//Modified by DSM(Sogeti)-2015x.4.1 Feb downtime for Defect Id-15145 -- ENDS
							
							//IPM V2/V3 2013x.4 Modification for Defect ID 3466 - Ends
							// DSO 2013x.4- Removed editLink END
							
							//P&G: Modified for defect No 441: End
							//P&G: Modification starts by PLM v2 -Modify for Sorting :End
							//Merged by 2013x upgrade team end -
							stringBuffer.append(strRelId);
							//P&G: Modified V3.1.2 for defect No 441: Start
							stringBuffer.append("&amp;removeHeader=Yes");
							//P&G: Modified V3.1.2 for defect No 441: End
							stringBuffer.append("&amp;header=");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							/*stringBuffer.append("%3Ctable%3E");
							stringBuffer.append("%3Ctr%3E ");
							stringBuffer.append("%3Ctd%3E ");*/
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							String strMsg="Substitute for";
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							//stringBuffer.append("%3Cb%3E" +strMsg+ "%3C%2Fb%3E");	
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							stringBuffer.append(" "+strMsg+" ");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							//stringBuffer.append("%3Ctd%3E%3Cb%3E%3A%3C%2Fb%3E%3C%2Ftd%3E"); //for :
							//stringBuffer.append("%3Ctd%3E%3Cb%3E" +strObjName + "%3C%2Fb%3E%3C%2Ftd%3E");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							stringBuffer.append(" " +strObjName + " ");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							/*stringBuffer.append("%3C%2Ftd%3E");
							stringBuffer.append("%3Ctd%3E%3Cb%3E%7C%3C%2Fb%3E%3C%2Ftd%3E");//for |
							stringBuffer.append("%3Ctd%3E");*/
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							//DSO 2013x.4 : Added the code to change the display labels for Description : Start
							String strMsg1 = DomainConstants.EMPTY_STRING;
							if(slTypes.contains(strObjectType)){
							
							     strSubHeaderPart = i18nNow.getI18nString ("emxCPN.BOM.SubstitutesTable.DescriptionHeaderLabel", "emxCPNStringResource", context.getLocale().getLanguage());
							     strMsg1 =i18nNow.getI18nString ("emxCPN.Common.Title", "emxCPNStringResource", context.getLocale().getLanguage());
							
							 }
							 else
							 {
							     strMsg1=i18nNow.getI18nString ("emxCPN.Label.SAPDescription", "emxCPNStringResource", context.getLocale().getLanguage());
							
							 }
							 //DSO 2013x.4 : Added the code to change the display labels for Description : End
							 // Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							//stringBuffer.append("%3Cb%3E" +strMsg1+ "%3C%2Fb%3E");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							stringBuffer.append(" " +strMsg1+ " ");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							/*stringBuffer.append("%3C%2Ftd%3E");
							stringBuffer.append("%3Ctd%3E%3Cb%3E%3A%3C%2Fb%3E%3C%2Ftd%3E");//for :					
							stringBuffer.append("%3Ctd%3E ");*/
							//stringBuffer.append("%3Ctd%3E%3Cb%3E" +strAttrTitleValue + "%3C%2Fb%3E%3C%2Ftd%3E");			
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							stringBuffer.append(" " +strAttrTitleValue + " ");			
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
							//stringBuffer.append("%3C%2Ftr%3E");
							// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
							//DSO : Added to display the title correctly on Substitute table page - START
							sbTitle.append (strMsg).append("|").append(strMsg1).append("|").append(strAttrTitleValue);
							//DSO : Added to display the title correctly on Substitute table page - END
							
							if(!strTypeBaseFormula.equalsIgnoreCase(strObjectType))
							{
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
								/*stringBuffer.append("%3Ctr%3E ");
								stringBuffer.append("%3Ctd%3E ");	
								stringBuffer.append("%3Cb%3E" +strSubHeader+ "%3C%2Fb%3E");*/					
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
								stringBuffer.append(" " +strSubHeader+ " ");					
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
								//stringBuffer.append("%3Ctd%3E%3Cb%3E%3A%3C%2Fb%3E%3C%2Ftd%3E"); //for :					
								//stringBuffer.append("%3Ctd%3E%3Cb%3E" +strpgPhaseName + "%3C%2Fb%3E%3C%2Ftd%3E");
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
								stringBuffer.append(" " +strpgPhaseName + " ");
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
								//stringBuffer.append("%3C%2Ftd%3E");					
								//stringBuffer.append("%3Ctd%3E%3Cb%3E%7C%3C%2Fb%3E%3C%2Ftd%3E");//for |
								//stringBuffer.append("%3Ctd%3E");
								//stringBuffer.append("%3Cb%3E" +strSubHeaderPart+ "%3C%2Fb%3E");					
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
								stringBuffer.append(" " +strSubHeaderPart+ " ");					
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
								/*stringBuffer.append("%3C%2Ftd%3E");
								stringBuffer.append("%3Ctd%3E%3Cb%3E%3A%3C%2Fb%3E%3C%2Ftd%3E");//for :					
								stringBuffer.append("%3Ctd%3E ");*/										
								//stringBuffer.append("%3Ctd%3E%3Cb%3E" +strpgPhaseDesc + "%3C%2Fb%3E%3C%2Ftd%3E");
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
								stringBuffer.append(" " +strpgPhaseDesc + " ");
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
								//stringBuffer.append("%3C%2Ftr%3E");
								// Commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
								//DSO : Added to display the title correctly on Substitute table page - START
								sbTitle.append("|").append(strSubHeader).append("|").append(strpgPhaseName).append("|").append(strSubHeaderPart).append("|").append(strpgPhaseDesc);
								//DSO : Added to display the title correctly on Substitute table page - END
							}
							//stringBuffer.append("%3C%2Ftable%3E");
							//DSO : Added to display the title correctly on Substitute table page - START
							stringBuffer.append("&amp;title=").append(sbTitle);
							//DSO : Added to display the title correctly on Substitute table page - END
						
							stringBuffer.append("', '");
							stringBuffer.append("900");
							stringBuffer.append("', '");
							stringBuffer.append("400");
							stringBuffer.append("', 'false', '");
							stringBuffer.append("popup");
							stringBuffer.append("')");
							stringBuffer.append("\">");
							stringBuffer.append("Yes");
							stringBuffer.append("</a>");
						}
						vcSubstituteFlag.add(stringBuffer.toString());
					}
					else
					{
						vcSubstituteFlag.add("No");
					}
				}
			}
			else
			{
			//DSO 2013x.4-Modification Done to view default value as "No" under substitute column for the newly added Product data Part- START
				vcSubstituteFlag.add("No");
			//DSO 2013x.4-Modification Done to view default value as "No" under substitute column for the newly added Product data Part- END 	
			}
			// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
			// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		//}else {vcSubstituteFlag.add("No Access");}
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		}
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		//}else {vcSubstituteFlag.add("No Access");}
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
		}
		catch(Exception e){
		throw e;
		}finally{
		if(isContextPushed){
		ContextUtil.popContext(context);
		}
		}
		return vcSubstituteFlag;
	}
	/**
	 * This is the table method for EBOM Part's Substitute from main EBOM table
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public MapList getTableEBOMPartsSubstituteList(Context context, String[] args) throws Exception
	{
		MapList mapList = new MapList();
		try {
			HashMap paramMap = (HashMap)JPO.unpackArgs(args);
			StringList slEBOMSubs                   = null;
			String sEBOMID=(String)paramMap.get("relId");
			DomainRelationship drelEBOMSub = new DomainRelationship(sEBOMID);
			String relationshipIds[] = new String[1];
			relationshipIds[0] = sEBOMID;
			StringList slrelationshipSelects = new StringList();
			slrelationshipSelects.addElement("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id");
			MapList mlEBOMSubInfo  =DomainRelationship.getInfo(context,relationshipIds,slrelationshipSelects);
			for(int i=0;i<mlEBOMSubInfo.size();i++)
			{
				StringList strEBOMSubRelID = new StringList();
				Map mapEBOMSubObject = (Map)mlEBOMSubInfo.get(i);
				Object objEBOMSubRelID = (Object)mapEBOMSubObject.get("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id");
				if("java.lang.String".equalsIgnoreCase((objEBOMSubRelID.getClass()).getName()))
				{
					strEBOMSubRelID.addElement((String)mapEBOMSubObject.get("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id"));
				}
				else
				{
					strEBOMSubRelID = (StringList)mapEBOMSubObject.get("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id");
				}
				String arrRelIDs[] = new String[strEBOMSubRelID.size()];
				for(int j=0;j<strEBOMSubRelID.size();j++)
				{
					arrRelIDs[j] = (String)strEBOMSubRelID.get(j);
				}
				StringList slEBOMSubRelSelect = new StringList(10);
				 //DSO : Added to refresh the Edit Substitute table after editing the validUntilDate - START
				slEBOMSubRelSelect.addElement("id");
				 //DSO : Added to refresh the Edit Substitute table after editing the validUntilDate - END
				slEBOMSubRelSelect.addElement("to.id");
				slEBOMSubRelSelect.addElement("attribute[pgChange]");
				slEBOMSubRelSelect.addElement("attribute[pgSubstituteCombinationNumber]");
				slEBOMSubRelSelect.addElement("attribute[pgQuantity]");
				slEBOMSubRelSelect.addElement("attribute[pgQuantityUnitOfMeasure]");
				slEBOMSubRelSelect.addElement("attribute[pgIncludeInCOSAnalysis]");
				slEBOMSubRelSelect.addElement("attribute[pgValidUntilDate]");
				slEBOMSubRelSelect.addElement("attribute[Comment]");
				slEBOMSubRelSelect.addElement("attribute[pgCalcQuantity]");				
				slEBOMSubRelSelect.addElement("attribute[pgMinQuantity]");
				slEBOMSubRelSelect.addElement("attribute[pgMaxQuantity]");
				slEBOMSubRelSelect.addElement("attribute[pgPositionIndicator]");
				slEBOMSubRelSelect.addElement("attribute[pgMaterialFunction]");
				//P&G: Modification starts by PLM v2 -PDF view Substitute
				slEBOMSubRelSelect.addElement("to.name");
				slEBOMSubRelSelect.addElement("to.type");
				slEBOMSubRelSelect.addElement("to.attribute[Title]");
				slEBOMSubRelSelect.addElement("to.attribute[pgCSSType]");
				slEBOMSubRelSelect.addElement("to.attribute[pgAssemblyType]");
				slEBOMSubRelSelect.addElement("to.attribute[pgFinishedProductCode]");
				
				//DSO 2013x.4 - Added the code to get Reference Designator,Base Unit of Measure and originating source attribute for Substitute Parts : Start
				slEBOMSubRelSelect.addElement("attribute["+DomainConstants.ATTRIBUTE_REFERENCE_DESIGNATOR+"]");
				slEBOMSubRelSelect.addElement("to.attribute["+${CLASS:pgDSOConstants}.ATTR_PG_BASEUOM+"]");
				slEBOMSubRelSelect.addElement("to.from["+${CLASS:pgDSOConstants}.REL_PG_PDTEMPLATES_TO_PGPLIBUOM+"].to.name");
				slEBOMSubRelSelect.addElement("to.attribute["+${CLASS:pgDSOConstants}.ATTR_PG_ORIGINATINGSOURCE+"]");
				//DSO 2013x.4 - Added the code to get Reference Designator,Base Unit of Measure and originating source attribute for Substitute Parts : End
				//P&G: Modification ends
				//DSM(DS) 2015x.1 - Added the code to show Quantity : Start
				slEBOMSubRelSelect.addElement("attribute[Quantity]");
				//DSM(DS) 2015x.1 - Added the code to show Quantity : End
				MapList mlEBOMSubRelInfo = DomainRelationship.getInfo(context,arrRelIDs,slEBOMSubRelSelect);
				//Merged by 2013x upgrade team start -
				//P&G: Modification starts by PLM v2 -Modify for Sorting :Start
				mlEBOMSubRelInfo.sort("attribute[pgSubstituteCombinationNumber]","ascending","integer");
				//P&G: Modification starts by PLM v2 -Modify for Sorting :End
				//Merged by 2013x upgrade team end -
				for(int count=0;count<mlEBOMSubRelInfo.size();count++)
				{
					Map mapEBOMSubRelInfo = (Map)mlEBOMSubRelInfo.get(count);
					Map tempMap = new HashMap();
					//DSO : Added to refresh the Edit Substitute table after editing the validUntilDate - START
					tempMap.put("EBOMSubstituteRelId",(String)mapEBOMSubRelInfo.get("id"));
					//DSO : Added to refresh the Edit Substitute table after editing the validUntilDate - END
					tempMap.put("id",(String)mapEBOMSubRelInfo.get("to.id"));
					tempMap.put("Chg",(String)mapEBOMSubRelInfo.get("attribute[pgChange]"));
					tempMap.put("SCN",(String)mapEBOMSubRelInfo.get("attribute[pgSubstituteCombinationNumber]"));
					//DSM(DS) 2015x.1 - Added the code to show Quantity : Start
					tempMap.put("Quantity",(String)mapEBOMSubRelInfo.get("attribute[Quantity]"));
					//DSM(DS) 2015x.1 - Added the code to show Quantity : End
					tempMap.put("UoM",(String)mapEBOMSubRelInfo.get("attribute[pgQuantityUnitOfMeasure]"));
					tempMap.put("COSAnalysis",(String)mapEBOMSubRelInfo.get("attribute[pgIncludeInCOSAnalysis]"));
					tempMap.put("ValidUntil",(String)mapEBOMSubRelInfo.get("attribute[pgValidUntilDate]"));
					tempMap.put("Comment",(String)mapEBOMSubRelInfo.get("attribute[Comment]"));
					tempMap.put("Min",(String)mapEBOMSubRelInfo.get("attribute[pgMinQuantity]"));
					tempMap.put("Max",(String)mapEBOMSubRelInfo.get("attribute[pgMaxQuantity]"));
					tempMap.put("Target",(String)mapEBOMSubRelInfo.get("attribute[pgQuantity]"));
					tempMap.put("PositionIndicator",(String)mapEBOMSubRelInfo.get("attribute[pgPositionIndicator]"));
					tempMap.put("MaterialFunction",(String)mapEBOMSubRelInfo.get("attribute[pgMaterialFunction]"));
					//P&G: Modification starts by PLM V2-PDF view Substitute
					tempMap.put("FinishedProductCode",(String)mapEBOMSubRelInfo.get("to.attribute[pgFinishedProductCode]"));
					tempMap.put("name",(String)mapEBOMSubRelInfo.get("to.name"));
					tempMap.put("Type",(String)mapEBOMSubRelInfo.get("to.type"));
					//Added by DSM(Sogeti)-2015x.1 for Substitutes table missing data (Defect ID-6863) - Starts
					tempMap.put("type",(String)mapEBOMSubRelInfo.get("to.type"));
					//Added by DSM(Sogeti)-2015x.1 for Substitutes table missing data (Defect ID-6863) - Ends
					tempMap.put("SAPDesc",(String)mapEBOMSubRelInfo.get("to.attribute[Title]"));
					tempMap.put("SS",(String)mapEBOMSubRelInfo.get("to.attribute[pgCSSType]"));
					tempMap.put("pgAssemblyType",(String)mapEBOMSubRelInfo.get("to.attribute[pgAssemblyType]"));
            		//DSO 2013x.4 - Added the code to get Reference Designator,Base Unit of Measure and originating source attribute for Substitute Parts : Start
					tempMap.put("Reference Designator",(String)mapEBOMSubRelInfo.get("attribute[Reference Designator]"));
                    tempMap.put("pgBaseUnitOfMeasure",(String)mapEBOMSubRelInfo.get("to.attribute[pgBaseUnitOfMeasure]"));
                    tempMap.put("pgBaseUnitOfMeasurePickList",(String)mapEBOMSubRelInfo.get("to.from[pgPDTemplatestopgPLIBUOM].to.name"));
                    tempMap.put("pgOriginatingSource",(String)mapEBOMSubRelInfo.get("to.attribute[pgOriginatingSource]"));
                    //DSO 2013x.4 - Added the code to get Reference Designator,Base Unit of Measure and originating source attribute for Substitute Parts : End
					//P&G: Modification ends
					mapList.add(tempMap);
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return mapList;
	}
	/**
	 * This API will return qty of substitute part in pgIPMEBOMPartsSubstituteTable Table
	 *
	 * @param context
	 * @param args
	 * @return vector having all row qty data
	 * @throws Exception
	 */
	public Vector getColumnSubstitutePartQuantityData(Context context, String[] args) throws Exception
	{
		HashMap paramMap = (HashMap)JPO.unpackArgs(args);
		Map ListMap =(Map)paramMap.get("paramList");
		String sEBOMID=(String)ListMap.get("relId");
		String strCommand       = "print connection "+sEBOMID+" select frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id dump |";
		String strMessage          = MqlUtil.mqlCommand(context,strCommand);
		StringList slEBOMSubs                  = FrameworkUtil.split(strMessage,"|");
		Vector vc = new Vector();
		for(int i=0;i < slEBOMSubs.size();i++)
		{
			String strEBOMSubstituteId=(String)slEBOMSubs.get(i);
			StringList slList = new StringList();
			slList.add(DomainConstants.SELECT_ATTRIBUTE_QUANTITY);
			MapList mlList=DomainRelationship.getInfo(context, new String[]{strEBOMSubstituteId}, slList);
			for(int j=0;j<mlList.size();j++)
			{
				Map map=(Map)mlList.get(j);
				String strQty=(String)map.get(DomainConstants.SELECT_ATTRIBUTE_QUANTITY);
				vc.add(strQty);
			}
		}
		return vc;
	}
	/**
	 * This function will return column data for ingredient loss in EBOM table
	 *
	 * @param context
	 * @param args
	 * @return vector having data for ingredient loss, Subtotal and Total
	 * @throws Exception
	 */
	public Vector getColumnIngredientLossData(Context context,String[] args) throws Exception
	{
		HashMap paramMap = (HashMap)JPO.unpackArgs(args);
		Map paramList = (Map) paramMap.get("paramList");
		String parentId = (String) paramList.get("objectId");
		MapList objectList = (MapList) paramMap.get("objectList");
		Vector<String> vcIngredientLossData = new Vector<String>(objectList.size());
		String strpgIngredientLoss = "";
		String strpgIngredientLossUoM = "";
		String strIngredientLossComment = "";
		String strSubTotal = "";
		String strSubTotalUoM = "";
		String strTotal = "";
		String strTotalUoM = "";
		String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strAttrpgIngredientLoss = PropertyUtil.getSchemaProperty("attribute_pgIngredientLoss");
		String strAttrpgIngredientLossUoM = PropertyUtil.getSchemaProperty("attribute_pgIngredientLossUnitOfMeasure");
		String strAttrpgSubTotal = PropertyUtil.getSchemaProperty("attribute_pgSubTotal");
		String strAttrpgSubTotalUoM = PropertyUtil.getSchemaProperty("attribute_pgSubTotalUnitOfMeasure");
		String strAttrpgTotal = PropertyUtil.getSchemaProperty("attribute_pgQuantity");
		String strAttrpgTotalUoM = PropertyUtil.getSchemaProperty("attribute_pgQuantityUnitOfMeasure");
		String strAttrpgIngredientLossComment = PropertyUtil.getSchemaProperty("attribute_pgIngredientLossComment");
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			Map object = (Map) iterator.next();
			String objectId = (String) object.get(DomainConstants.SELECT_ID);
			String strLevel = (String) object.get("level");
			int intLevel = Integer.parseInt(strLevel);
			DomainObject dObjFIL = new DomainObject(objectId);
			String strType = dObjFIL.getInfo(context,DomainConstants.SELECT_TYPE);
			if(strtypePGPhase.equalsIgnoreCase(strType) && intLevel <= 1)
			{
				Map mapTestData = dObjFIL.getAttributeMap(context);
				//Get Ingredient Loss data
				strpgIngredientLoss = (String)mapTestData.get(strAttrpgIngredientLoss);
				strpgIngredientLossUoM = (String)mapTestData.get(strAttrpgIngredientLossUoM);
				strIngredientLossComment = (String)mapTestData.get(strAttrpgIngredientLossComment);
				//Get Subtotal data
				strSubTotal = (String)mapTestData.get(strAttrpgSubTotal);
				strSubTotalUoM = (String)mapTestData.get(strAttrpgSubTotalUoM);
				//Get Total data
				strTotal = (String)mapTestData.get(strAttrpgTotal);
				strTotalUoM = (String)mapTestData.get(strAttrpgTotalUoM);
				vcIngredientLossData.add("<b>Subtotal:</b> "+strSubTotal+" "+strSubTotalUoM+"<br><b>Ingredient Loss:</b> "+strpgIngredientLoss+" "+strpgIngredientLossUoM+"</br><b>Ingredient Loss Comment:   </b>"+strIngredientLossComment+"<br> <b>Total:</b> "+strTotal+" "+strTotalUoM+"</br>");
			}
			else
			{
				vcIngredientLossData.add("");
			}
		}
		return vcIngredientLossData;
	}
	/**
	 * This function will return column data for Material Function and Position Indicator
	 *
	 * @param context
	 * @param args
	 * @return vector having data for Material function and position indicator
	 * @throws Exception
	 */
	public Vector getMFPI(Context context,String args[]) throws Exception
	{
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		MapList objectList = (MapList)programMap.get("objectList");
		Vector mfpiVector = new Vector(objectList.size());
		Iterator bomListItr = objectList.iterator();
		String strMaterialFunction = "";
		String strPositionIndicator = "";
		String strMFPI = "";
		String strType = "";
		String strAttrpgMF = PropertyUtil.getSchemaProperty("attribute_pgMaterialFunction");
		String strAttrpgPI = PropertyUtil.getSchemaProperty("attribute_pgPositionIndicator");
		String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		while(bomListItr.hasNext())
		{
			Map bomMap = (Map)bomListItr.next();
			strType = (String)bomMap.get(DomainConstants.SELECT_TYPE);
			String strRelId = (String) bomMap.get(DomainConstants.SELECT_RELATIONSHIP_ID);
			strMaterialFunction = "";
			if (strRelId != null && strRelId.length() > 0)
			{
				try
				{
					DomainRelationship doRel = new DomainRelationship(strRelId);
					strMaterialFunction = doRel.getAttributeValue(context, strAttrpgMF);
					strPositionIndicator = doRel.getAttributeValue(context, strAttrpgPI);
					if (strtypePGPhase.equalsIgnoreCase(strType))
					{
						strMFPI = strMaterialFunction+"<br>"+strPositionIndicator+"</br>";
						mfpiVector.addElement(strMFPI);
					}
					else
					{
						mfpiVector.addElement("");
					}
				}
				catch (Exception excep)
				{
					strMFPI = "";
				}
			}
		}
		return mfpiVector;
	}
	/**
	 * This function will return column data for Comment
	 *
	 * @param context
	 * @param args
	 * @return vector having data for Comments along with Ingredient Loss Comment (if object is FIL)
	 * @throws Exception
	 */
	public Vector getCommentColumnData(Context context,String[] args) throws Exception
	{
		HashMap paramMap = (HashMap)JPO.unpackArgs(args);
		Map paramList = (Map) paramMap.get("paramList");
		String strReportFormat = (String)paramList.get("reportFormat");
		String parentId = (String) paramList.get("objectId");
		
		//V2/V3 2013x.4 - Added for Defect #2636 to display comments of BSF type by V2/V3 IPM team Starts
		String table = (String) paramList.get("table");
		//V2/V3 2013x.4 - Added for Defect #2636 to display comments of BSF type by V2/V3 IPM team Ends
		MapList objectList = (MapList) paramMap.get("objectList");
		Vector<String> vcCommentData = new Vector<String>(objectList.size());
		//Define variables for Comment and Ingredient Loss Comment
		String strObjectComment = "";
		String strIngLossComment = "";
		String strIngLossComments = "";
		String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strtypePGBSF = PropertyUtil.getSchemaProperty("type_pgBaseFormula");
		String strAttrComment = PropertyUtil.getSchemaProperty("attribute_Comment");
		String strAttrIngredientLossComment  = PropertyUtil.getSchemaProperty("attribute_pgIngredientLossComment");
		String strAttrIngredientLossComments = PropertyUtil.getSchemaProperty("attribute_pgIngredientLossComments");
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try {
			ContextUtil.pushContext(context);
			isContextPushed = true;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			for(Iterator itrObjList = objectList.iterator(); itrObjList.hasNext(); )
			{
				StringBuffer strFinalComment = new StringBuffer();
				Map mapObject = (Map)itrObjList.next();
				String strRootNode=(String)mapObject.get("Root Node");
				String strObjectType=(String)mapObject.get("type");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				/*String strLevel= (String)mapObject.get("level");
				int intLevel = Integer.parseInt(strLevel);*/
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				String strObjectID = (String)mapObject.get(DomainConstants.SELECT_ID);
				
				//V2/V3 2013x.4 - Added for Defect #2636 to display comments of BSF type by V2/V3 IPM team Starts
				if(strObjectType==null)
				{
					strObjectType =  MqlUtil.mqlCommand(context, "print bus " + strObjectID + " select type dump");
				}
			   //V2/V3 2013x.4 - Added for Defect #2636 to display comments of BSF type by V2/V3 IPM team Ends
				String strConnectionID = (String)mapObject.get("id[connection]");
				//Create DomainObject to get required data and get attribute map
				DomainObject dobjObjectForComment = DomainObject.newInstance(context,strObjectID);
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
				//if(FrameworkUtil.hasAccess(context, dobjObjectForComment , "read")!=false){
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
				Map mapAttributeData = (Map)dobjObjectForComment.getAttributeMap(context);
				strObjectComment = (String)mapAttributeData.get(strAttrComment);
				strIngLossComment = (String)mapAttributeData.get(strAttrIngredientLossComment);
				strIngLossComments = (String)mapAttributeData.get(strAttrIngredientLossComments);
				if(!"true".equalsIgnoreCase(strRootNode))
				{
					DomainRelationship domrelEBOM = new DomainRelationship(strConnectionID);
					String strValue = (String)domrelEBOM.getAttributeValue(context, strAttrComment);
					if(strtypePGPhase.equalsIgnoreCase(strObjectType))
					{
						// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
						// Removed if condition to remove the level check
						//if(intLevel == 1)
						//{
						// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
						if(strReportFormat==null){
							strObjectComment=strObjectComment.replaceAll("<","&lt;");
							strObjectComment=strObjectComment.replaceAll(">","&gt;");
							strObjectComment=strObjectComment.replaceAll("&","&amp;"); //Added for defect 5008 - Special characters are not displayd properly in Comments column in BOM table
							strFinalComment.append(strObjectComment);
							// Added for Defect #5735 to display special characters properly in Comments Column in BOM table for Release 2013x.3.1 by IPM Team Start
							strIngLossComment=strIngLossComment.replaceAll("<","&lt;");
							strIngLossComment=strIngLossComment.replaceAll(">","&gt;");
							strIngLossComment=strIngLossComment.replaceAll("&","&amp;");
							// Added for Defect #5735 to display special characters properly in Comments Column in BOM table for Release 2013x.3.1 by IPM Team End
							strFinalComment.append("<br><b>Ingredient Loss Comment: </b>"+strIngLossComment+"</br>");
						}
						else if(strReportFormat.equalsIgnoreCase("CSV")){
							strFinalComment.append(strObjectComment);
							strFinalComment.append("\nIngredient Loss Comment:"+strIngLossComment);
						}
						// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
						/*}
						else
						{
							strFinalComment.append(strValue);
						}*/
						// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
					}
					else
					{
						strFinalComment.append(strValue);
					}
				}
				else
				{
					if(strtypePGBSF.equalsIgnoreCase(strObjectType))
					{
						if(strReportFormat==null){
							// Added for Defect #5735 to display special characters properly in Comments Column in BOM table for Release 2013x.3.1 by IPM Team Start
							strIngLossComments=strIngLossComments.replaceAll("<","&lt;");
							strIngLossComments=strIngLossComments.replaceAll(">","&gt;");
							strIngLossComments=strIngLossComments.replaceAll("&","&amp;");
							// Added for Defect #5735 to display special characters properly in Comments Column in BOM table for Release 2013x.3.1 by IPM Team End
							//V2/V3 2013x.4 - Modified for Defect #2636 to display comments of BSF type by V2/V3 IPM team Starts
							if(table.equalsIgnoreCase("pgIPMBaseFormulaBOMTable"))
							{
								strFinalComment.append("<b>Ingredient Loss Comment: </b>"+strIngLossComments);
							}
							else
							{
								strFinalComment.append("<br><b>Ingredient Loss Comment: </b>"+strIngLossComments+"</br>");
							}
							//V2/V3 2013x.4 - Modified for Defect #2636 to display comments of BSF type by V2/V3 IPM team Ends
							
						}else if(strReportFormat.equalsIgnoreCase("CSV")){
							strFinalComment.append("Ingredient Loss Comment:"+strIngLossComments);
						}
					}
				}
				
			//}
			// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
			//else {strFinalComment.append("No Access"); }
			// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
			vcCommentData.add(strFinalComment.toString());
			}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (isContextPushed){
				ContextUtil.popContext(context);
			}
		}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vcCommentData;
	}
	/**
	 * This function will return the vector of checked in files in the Reference Document
	 *
	 * @param context
	 * @param args
	 * @return vector  - Checked in files of related Reference Document
	 * @throws Exception
	 */
	 //Modified by DSM(Sogeti)-2015x.1 on 17-05-16 for performance improvement and ArryIndexOutOfBoundException - starts
	public static Vector getBomGenDocViewLink(Context context, String[] args) throws Exception {
		Vector fileActionsVector = new Vector();
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		try {
			// Added for Defect fix 9742 by DSM 2015x.2(Sogeti) Starts
			boolean canViewAndDownload = true;
			if(context.isAssigned(pgV3Constants.ROLE_PGCONTRACTMANUFACTURER)){
			canViewAndDownload = false;
			}
			// Added for Defect fix 9742 by DSM 2015x.2(Sogeti) Ends 
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList objectList = (MapList) programMap.get("objectList");
			Map paramList = (Map) programMap.get("paramList");
			String ObjectId = (String) paramList.get("objectId");
			//Code Merge Start for 2013x Upgrade
			//String strTableName = (String)paramList.get("table");
			//Code Merge Ends for 2013x Upgrade
			Iterator objectListItr = objectList.iterator();
			//String languageStr = (String) paramList.get("languageStr");
			String masterId = null;
			StringList fileNameList = null;
			StringList fileFormatList = null;
			StringBuffer strViewerURL = null;
			//boolean canViewAndDownload = false;
			int iListSize = 0;
			//Pattern relPattern = new Pattern("");
			//relPattern.addPattern(PropertyUtil.getSchemaProperty(context, CommonDocument.SYMBOLIC_relationship_ReferenceDocument));
			StringList typeSelects = new StringList(3);
			typeSelects.add(CommonDocument.SELECT_ID);
			typeSelects.add(DomainConstants.SELECT_FILE_NAME);
			typeSelects.add(DomainConstants.SELECT_FILE_FORMAT);
			//Code Merge Start for 2013x Upgrade
			//P&G: Modification starts by PLM v2 -Modify for remove Security check :Start
			//typeSelects.add("current.access[checkout]");
			//P&G: Modification starts by PLM v2 -Modify for remove Security check :End
			//Code Merge End for 2013x Upgrade
			//StringList relSelects = new StringList(1);
			//relSelects.add(CommonDocument.SELECT_RELATIONSHIP_ID);
			// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
			ContextUtil.pushContext(context);
			isContextPushed = true;
			// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			int i = 0;
			int size = 0;
			Map objectMap = null;
			Map tempMap = null;
			DomainObject domObj;
			StringList slSelects = new StringList(4);
			slSelects.add(DomainConstants.SELECT_TYPE);
			slSelects.add(DomainConstants.SELECT_NAME);
			slSelects.add(DomainConstants.SELECT_REVISION);
			slSelects.add(pgV3Constants.SELECT_ATTRIBUTE_PGLEGACYGCAS);
			Map mBasicInfo = null;
			String rev = "";
			String name = "";
			String strObjectType = "";
			//String fileName = "";
			StringBuffer fileName;
			String objectWhere = "";
			String strRootNode = "";
			MapList docList = new MapList();
			while (objectListItr.hasNext()) {
				Object oTemp = objectListItr.next();
				try{
					tempMap = (Hashtable) oTemp;
				}catch(Exception exec){
					tempMap = (HashMap) oTemp;
				}
				boolean isHasAccess = false;
				masterId = (String) tempMap.get("id");
				strRootNode=(String)tempMap.get("Root Node");
				// Modifed for Defect fix 16614 by DSM 2015x.5(Sogeti) Starts
				//code to skip the No Access data row values from the return list
				String strRowAccess = (String)tempMap.get("objReadAccess");
				if(!"true".equalsIgnoreCase(strRowAccess))
				{					
					continue;		
				}
				// Modifed for Defect fix 16614 by DSM 2015x.5(Sogeti) Ends
				
				// Modifed for Defect fix 9742 by DSM 2015x.2(Sogeti) Starts
				domObj = DomainObject.newInstance(context, masterId);
				if(canViewAndDownload){
				isHasAccess = FrameworkUtil.hasAccess(context, domObj , "read");
				if(isHasAccess){
					mBasicInfo   = (Map) domObj.getInfo(context,slSelects);
				}
				}
				// Modifed for Defect fix 9742 by DSM 2015x.2(Sogeti) Ends
				if(!"true".equalsIgnoreCase(strRootNode) && isHasAccess) {
					try {
						rev 	 = (String)mBasicInfo.get(DomainConstants.SELECT_REVISION);
						name 	 = (String)mBasicInfo.get(DomainConstants.SELECT_NAME);
						strObjectType = (String)mBasicInfo.get(DomainConstants.SELECT_TYPE);

						//fileName = "LR_" + name + "." + rev;
						fileName = new StringBuffer();
						fileName.append("LR_");
						fileName.append(name);
						fileName.append(".");
						fileName.append(rev);
						objectWhere = "name=='" +  fileName.toString() + "' && revision=='Rendition'";

						docList = domObj.getRelatedObjects(context, DomainConstants.RELATIONSHIP_REFERENCE_DOCUMENT, pgV3Constants.TYPE_PGIPMDOCUMENT, typeSelects, null, false, true, (short) 1, objectWhere, null, null, null, null);

						//P&G: Modification for V4 starts. As per FPC to IPS conversion, FP name can be changed to pgFinishedProductCode attribute value. In this case, gendoc is not displayed correctly. Need to check for pgLegacyGCAS attribute also
						if (pgV3Constants.TYPE_PGFINISHEDPRODUCT.equals(strObjectType) && (docList == null || docList.size() == 0)) {
							String strLegacyGCAS = (String)mBasicInfo.get(pgV3Constants.SELECT_ATTRIBUTE_PGLEGACYGCAS);
							if(null != strLegacyGCAS && !"".equalsIgnoreCase(strLegacyGCAS)) {
								//fileName = "LR_" + strLegacyGCAS + "." + rev;
								fileName = new StringBuffer();
								fileName.append("LR_");
								fileName.append(strLegacyGCAS);
								fileName.append(".");
								fileName.append(rev);
								objectWhere = "name==" + fileName.toString() + " && revision==Rendition";
								docList = domObj.getRelatedObjects(context, DomainConstants.RELATIONSHIP_REFERENCE_DOCUMENT, pgV3Constants.TYPE_PGIPMDOCUMENT, typeSelects, null, false, true, (short) 1, objectWhere, null, null, null, null);
							}
						}
						if (docList != null && docList.size() > 0) {
							size = docList.size();
							for (i = 0; i < size; i++) {
							Object objTemp = docList.get(i);
							try{
									objectMap = (Hashtable) objTemp;
								}catch(Exception exec){
									objectMap = (HashMap) objTemp;
							}
							Object objFile = (Object) objectMap.get(DomainConstants.SELECT_FILE_NAME);
							Object objFormat = (Object) objectMap.get(DomainConstants.SELECT_FILE_FORMAT);
								if (objFile != null && !(objFile.equals("") || objFile.equals(" "))) {
								if (objFile instanceof String) {
										fileNameList = new StringList((String) objectMap.get(DomainConstants.SELECT_FILE_NAME));
										fileFormatList = new StringList((String) objectMap.get(DomainConstants.SELECT_FILE_FORMAT));
									} else {
										fileNameList = (StringList) objectMap.get(DomainConstants.SELECT_FILE_NAME);
										try {
											fileFormatList = (StringList) objectMap.get(DomainConstants.SELECT_FILE_FORMAT);
										} catch (Exception ex) {
											fileFormatList = new StringList((String) objectMap.get(DomainConstants.SELECT_FILE_FORMAT));
										}
								}
									strViewerURL = new StringBuffer();
								if ((fileNameList != null && fileNameList.size() > 0)) {
									iListSize = fileNameList.size();
									for (int j = 0; j < iListSize; j++) {
											strViewerURL.append(getViewerURLWithFileGenDocAsLink(context, (String) objectMap.get(DomainConstants.SELECT_ID),
										(String) fileFormatList.get(j), (String) fileNameList.get(j)));
									}
										fileActionsVector.add(strViewerURL.toString());
									} else {
										fileActionsVector.add("");
									}
								} else {
									fileActionsVector.add("");
								}
							} // FOR LOOP END
						} else { // if loop end
							fileActionsVector.add("");
						}
					} catch (Exception ex) {
						//No need to handle the exception as the objects with No Access are returning in to UI so getting exception while creating the DomainObject
					}
				} else { // if loop end
					fileActionsVector.add("");
				}
			}// while loop end
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
			if(isContextPushed){
		ContextUtil.popContext(context);
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			return fileActionsVector;
		}
	}
	//Modified by DSM(Sogeti)-2015x.1 on 17-05-16 for performance improvement and ArryIndexOutOfBoundException - Starts
/**
	 * This function will return the vector of checked in files
	 *  USED IN PM FILE SEARCH
	 *
	 * @param context
	 * @param args
	 * @return vector  - Checked in files
	 * @throws Exception
	 */
	public static Vector getBomGenDocViewLink_FileSearch(Context context, String[] args) throws Exception {
		Vector fileActionsVector = new Vector();
		try {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList objectList = (MapList) programMap.get("objectList");
			Map paramList = (Map) programMap.get("paramList");
			String ObjectId = (String) paramList.get("objectId");
			//Code Merge Start for 2013x Upgrade
			String strTableName = (String)paramList.get("table");
			//Code Merge Ends for 2013x Upgrade
			Iterator objectListItr = objectList.iterator();
			
			// Added for Defect fix 9742 by DSM 2015x.2(Sogeti) Starts
			boolean canViewAndDownload = true;
			if(context.isAssigned(pgV3Constants.ROLE_PGCONTRACTMANUFACTURER)){
			canViewAndDownload = false;
			}
			//Modified by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Starts
			//String masterId = null;
			String strTSId = null;
			//Modified by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638  Ends
			StringList fileNameList = null;
			StringList fileFormatList = null;
			StringBuffer strViewerURL = null;
			//boolean canViewAndDownload = false;
			int iListSize = 0;
			//Pattern relPattern = new Pattern("");
			//relPattern.addPattern(PropertyUtil.getSchemaProperty(context, CommonDocument.SYMBOLIC_relationship_ReferenceDocument));
			StringList typeSelects = new StringList(1);
			typeSelects.add(CommonDocument.SELECT_ID);
			typeSelects.add(DomainConstants.SELECT_FILE_NAME);
			typeSelects.add(DomainConstants.SELECT_FILE_FORMAT);
			int i = 0;
			int size = 0;
			Map objectMap = null;
			Map tempMap = null;
			while (objectListItr.hasNext()) {
				Object oTemp = objectListItr.next();
				try{
					tempMap = (Hashtable) oTemp;
				}catch(Exception exec){
					tempMap = (HashMap) oTemp;
				}
				//Modified by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Starts
				//masterId = (String) tempMap.get("id");
				strTSId = (String) tempMap.get("id");
				//Modified by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Ends
				String strRootNode=(String)tempMap.get("Root Node");
				if(!"true".equalsIgnoreCase(strRootNode))
				{				
					//Modified by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Starts
					Map finalMap = new HashMap();
					/*
					String strTSName = MqlUtil.mqlCommand(context, "print bus " + masterId + " select to[Reference Document].from.name dump");
					String strTSRev = MqlUtil.mqlCommand(context, "print bus " + masterId + " select to[Reference Document].from.revision dump");
					//P&G: 2013x.2 modification for FPC conversion
					String strTSType = MqlUtil.mqlCommand(context, "print bus " + masterId + " select to[Reference Document].from.type dump");
					String strTSId = MqlUtil.mqlCommand(context, "print bus " + masterId + " select to[Reference Document].from.id dump");
					*/
					//P&G:	End 2013x.2 modification for FP conversion
					DomainObject domObj = DomainObject.newInstance(context, strTSId);
					StringList slTSIdInfo = new StringList(3);
					slTSIdInfo.add(DomainConstants.SELECT_TYPE);
					slTSIdInfo.add(DomainConstants.SELECT_NAME);
					slTSIdInfo.add(DomainConstants.SELECT_REVISION);
					String strTSType = "";
					String strTSName = "";
					String strTSRev = "";
					Map mpObjInfo = null;
					if(canViewAndDownload){
						if(FrameworkUtil.hasAccess(context, domObj , "read")!=false){
							mpObjInfo = domObj.getInfo(context, slTSIdInfo);
						}
					}
					
					if(mpObjInfo != null){
					strTSType = (String)mpObjInfo.get(DomainConstants.SELECT_TYPE);
					strTSName = (String)mpObjInfo.get(DomainConstants.SELECT_NAME);
					strTSRev = (String)mpObjInfo.get(DomainConstants.SELECT_REVISION);
					}
					if(UIUtil.isNotNullAndNotEmpty(strTSName) && UIUtil.isNotNullAndNotEmpty(strTSRev)) {
					//Modified by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Ends
					String name = "LR_" + strTSName + "." + strTSRev;
					MapList docList = domObj.findObjects(context,"pgIPMDocument",name,"Rendition","*","*","",false,typeSelects);
					//P&G: 2013x.2 Modification starts. As per FPC to IPS conversion, FP name can be changed to pgFinishedProductCode attribute value. In this case, gendoc is not displayed correctly. Need to check for pgLegacyGCAS attribute also
					if (pgV3Constants.TYPE_PGFINISHEDPRODUCT.equals(strTSType) && (docList == null || docList.size() == 0))
					{						
						DomainObject domTSObj = DomainObject.newInstance(context, strTSId);
						String strLegacyGCAS = "";
						if(FrameworkUtil.hasAccess(context, domTSObj , "read")!=false){
						strLegacyGCAS = (String)domTSObj.getInfo(context, "attribute[pgLegacyGCAS]");
						}
						if(null != strLegacyGCAS && !"".equalsIgnoreCase(strLegacyGCAS))
						{
							name = "LR_" + strLegacyGCAS + "." + strTSRev;
							docList = domObj.findObjects(context,"pgIPMDocument",name,"Rendition","*","*","",false,typeSelects);							
						}
					} 
					//P&G: 2013x.2  Modification ends	
						//Added by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638  Starts
						strViewerURL = new StringBuffer();
						//Added by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Ends
					if (docList != null && docList.size() > 0) {
						size = docList.size();
						for (i = 0; i < size; i++) {
							Object objTemp = docList.get(i);
							try{
								objectMap = (Hashtable) objTemp;
							}catch(Exception exec){
								objectMap = (HashMap) objTemp;
							}
							Object objFile = (Object) objectMap.get(DomainConstants.SELECT_FILE_NAME);
							Object objFormat = (Object) objectMap.get(DomainConstants.SELECT_FILE_FORMAT);
							if (objFile != null && !(objFile.equals("") || objFile.equals(" "))) {
								if (objFile instanceof String) {
									fileNameList = new StringList((String) objectMap.get(DomainConstants.SELECT_FILE_NAME));
									fileFormatList = new StringList((String) objectMap.get(DomainConstants.SELECT_FILE_FORMAT));
								} else {
									fileNameList = (StringList) objectMap.get(DomainConstants.SELECT_FILE_NAME);
									try {
										fileFormatList = (StringList) objectMap.get(DomainConstants.SELECT_FILE_FORMAT);
									} catch (Exception ex) {
										fileFormatList = new StringList((String) objectMap.get(DomainConstants.SELECT_FILE_FORMAT));
									}
								}
								//Code Merge Start for 2013x Upgrade
								//P&G: Modification starts by PLM v2 -Modify for remove Security check :Start
								//canViewAndDownload = "true".equalsIgnoreCase((String) objectMap.get("current.access[checkout]"));
								//P&G: Modification starts by PLM v2 -Modify for remove Security check :End
									//commented by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Starts
									//strViewerURL = new StringBuffer();
									//commented by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638  Ends
								//P&G: Modification starts by PLM v2 -Modify for remove Security check :Start
								//if (canViewAndDownload && (fileNameList != null && fileNameList.size() > 0)) {
								if ((fileNameList != null && fileNameList.size() > 0)) {
									//P&G: Modification starts by PLM v2 -Modify for remove Security check :End
									//Code Merge End for 2013x Upgrade
									iListSize = fileNameList.size();
									for (int j = 0; j < iListSize; j++) {
										strViewerURL.append(getViewerURLWithFileGenDocAsLink(context, (String) objectMap.get(DomainConstants.SELECT_ID),
												(String) fileFormatList.get(j), (String) fileNameList.get(j)));
										//strViewerURL.append("<br>");
									}
									//Merged by 2013x upgrade team start -
									//StringList slURL = new StringList();
									//slURL.add(strViewerURL.toString());
									fileActionsVector.add(strViewerURL.toString());
									//Merged by 2013x upgrade team end -
								} else {
									fileActionsVector.add("");
								}
							} else {
								fileActionsVector.add("");
							}
						} // FOR LOOP END
						}
						//Added by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638  Starts
						else {
						fileActionsVector.add("");
						}
						//Added by V4-IPM/DSO-2013x.5 for Defects -170/1095/2227/3579/3635/3636/3637/3638 Ends
					} else { // if loop end
						fileActionsVector.add("");
					}
				} else { // if loop end
					fileActionsVector.add("");
				}
			}// while loop end
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			return fileActionsVector;
		}
	}	
	/**
	 * This function will return the icon to be displayed in TS column and URL for it in structure browser format
	 *
	 * @param context
	 * @param objectId - Object ID of reference document
	 * @param format - format of the checked in file
	 * @param fileName - name of the file to be checked out
	 * @return string  - URL of the file to be checked out
	 * @throws Exception
	 */
	public static String getViewerURLWithFileGenDocAsLink(Context context, String objectId, String format, String fileName) throws Exception {
		String returnURL = "";
		try {
			Map formatViewerMap = FormatUtil.getViewerCache(context);
			String strAmpersand = "%26";
			//Added null and blank check for Defect 4905 Starts
			if(null!=format && null!=fileName && !"".equals(format.trim()) && !"".equals(fileName.trim()) ){
				String URLParameters = "?action=view"+strAmpersand+"id=" + objectId + strAmpersand + "objectId=" + objectId + strAmpersand + "format=" + format + strAmpersand +"file=" + fileName + strAmpersand + "fileName="
				+ fileName;
				//Not Used anywhere in below code
				/*Map formatDetailsMap = (Map) formatViewerMap.get(format);
				if (formatDetailsMap == null) {
					FormatUtil.loadViewerCache(context);
				}
				formatDetailsMap = (Map) formatViewerMap.get(format);*/
				//String viewerURL = "";
				StringBuffer fileViewerURL = new StringBuffer(256);
				String aliasFormat = FrameworkUtil.getAliasForAdmin(context, "format", format, true);
				FormatUtil formatUtil = new FormatUtil(aliasFormat);
				String viewer = formatUtil.getViewerPreference(context, null);
				//&#34; is used for double quote as " is not allowed in structure browser
				String viewerURL = "javascript:showNonModalDialog(&#34;../cpn/pgIPMgenDocCheckout.jsp"+URLParameters+"&#34;)";
				fileViewerURL.append("<a href = \"" + viewerURL + "\" >");
				fileViewerURL.append("<img height='16' width='16' border='0' src='../common/images/iconActionPDF.gif' />");
				fileViewerURL.append("</a>");
				returnURL = fileViewerURL.toString();
			}
			//Added null and blank check for Defect 4905 Ends
			//return returnURL;
		} catch (Exception ex) {
			ex.printStackTrace();
			//throw ex;
		}
		finally {
			return returnURL;
		}
	}
	//Modified by DSM(Sogeti)-2015x.1 on 17-05-16 for performance improvement and ArryIndexOutOfBoundException - Starts
	/**
	 * This function will return column data for Change (Chg.)
	 *
	 * @param context
	 * @param args
	 * @return vector having data for attribute pgChange on EBOM relationship
	 * @throws Exception
	 */
	public Vector getChgColumnData(Context context,String[] args) throws Exception
	{
		Vector vcChgData = new Vector();
		Map paramMap = (Map)JPO.unpackArgs(args);
		MapList objectList = (MapList)paramMap.get("objectList");
		String strChgValue = "";
		String strAttrPGChange = PropertyUtil.getSchemaProperty("attribute_pgChange");
		String strRootNode ="";
		String strRelId ="";
		Map object = null;
		DomainRelationship dRelEBOM =null;
		for(Iterator itr = objectList.iterator(); itr.hasNext(); )
		{
			object = (Map) itr.next();
			strRootNode = (String)object.get("Root Node");
			strRelId = (String) object.get(DomainConstants.SELECT_RELATIONSHIP_ID);
			if(!"true".equalsIgnoreCase(strRootNode))
			{
				if(!"".equalsIgnoreCase(strRelId) && strRelId != null)
				{
					try
					{
						dRelEBOM = new DomainRelationship(strRelId);
						strChgValue = (String)dRelEBOM.getAttributeValue(context,strAttrPGChange);
						vcChgData.add(strChgValue);
					}
					catch (Exception e)
					{
						vcChgData.add("");
						System.out.println("\n Exception ocurred in method getChgColumnData "+e.toString());
					}
				}
				else
				{
					vcChgData.add("");
				}
			}
			else
			{
				vcChgData.add("");
			}
		}
		return  vcChgData;
	}
// Added for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
	/**
	 * This function will return column data for Material Function
	 *
	 * @param context
	 * @param args
	 * @return vector having data for attribute pgMaterialFunction on EBOM relationship
	 * @throws Exception
	 */
	public Vector getMaterialFunction(Context context,String[] args) throws Exception
	{
		Vector vcMatData = new Vector();
		Map paramMap = (Map)JPO.unpackArgs(args);
		MapList objectList = (MapList)paramMap.get("objectList");
		String strMatValue = "";
		String strAttrPGMat = PropertyUtil.getSchemaProperty("attribute_pgMaterialFunction");
		String strRelId ="";
		Map object = null;
		DomainRelationship dRelEBOM =null;
		for(Iterator itr = objectList.iterator(); itr.hasNext(); )
		{
			object = (Map) itr.next();
			strRelId = (String) object.get(DomainConstants.SELECT_RELATIONSHIP_ID);
				if(!"".equalsIgnoreCase(strRelId) && strRelId != null)
				{
					try
					{
						dRelEBOM = new DomainRelationship(strRelId);
						strMatValue = (String)dRelEBOM.getAttributeValue(context,strAttrPGMat);
						vcMatData.add(strMatValue);
					}
					catch (Exception e)
					{
						vcMatData.add("");
						System.out.println("\n Exception ocurred in method getChgColumnData "+e.toString());
					}
				}
				else
				{
					vcMatData.add("");
				}
		}
		return  vcMatData;
	}
	/**
	 * This function will return column data for Position Indicator
	 *
	 * @param context
	 * @param args
	 * @return vector having data for attribute pgPositionIndicator on EBOM relationship
	 * @throws Exception
	 */
	public Vector getPositionIndicator(Context context,String[] args) throws Exception
	{
		Vector vcPosData = new Vector();
		Map paramMap = (Map)JPO.unpackArgs(args);
		MapList objectList = (MapList)paramMap.get("objectList");
		String strPosValue = "";
		String strAttrPGPos = PropertyUtil.getSchemaProperty("attribute_pgPositionIndicator");
		String strRelId ="";
		Map object = null;
		DomainRelationship dRelEBOM =null;
		for(Iterator itr = objectList.iterator(); itr.hasNext(); )
		{
			object = (Map) itr.next();
			strRelId = (String) object.get(DomainConstants.SELECT_RELATIONSHIP_ID);
				if(!"".equalsIgnoreCase(strRelId) && strRelId != null)
				{
					try
					{
						dRelEBOM = new DomainRelationship(strRelId);
						strPosValue = (String)dRelEBOM.getAttributeValue(context,strAttrPGPos);
						vcPosData.add(strPosValue);
					}
					catch (Exception e)
					{
						vcPosData.add("");
						System.out.println("\n Exception ocurred in method getChgColumnData "+e.toString());
					}
				}
				else
				{
					vcPosData.add("");
				}
		}
		return  vcPosData;
	}
// Added for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends

	/**
	 * This API gives Abbreviation for type in Bill of Matetrial  Table
	 *
	 * @param context
	 * @param args
	 * @return vector
	 * @throws Exception
	 */
	public Vector getTypeNameColumnData(Context context,String[]args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strTypeName = PropertyUtil.getSchemaProperty("type_pgPhase");
		//To display abbr. for types in table column
		Map mapCodeMapping = new HashMap();
		mapCodeMapping.put("pgApprovedSupplierList", "ASL");
		mapCodeMapping.put("pgArtwork", "ART");
		mapCodeMapping.put("pgBaseFormula","BSF");
		mapCodeMapping.put("ECR","CR");
		mapCodeMapping.put("pgCommonPerformanceSpecification", "CPST");
		mapCodeMapping.put("pgConsumerDesignBasis","CDB");
		mapCodeMapping.put("pgFinishedProduct","FP");
		mapCodeMapping.put("pgIllustration", "ILST");
		mapCodeMapping.put("pgLogisticSpec", "LOG");
		mapCodeMapping.put("pgMakingInstructions","MI");
		mapCodeMapping.put("pgMasterPackingMaterial","MPMS");
		mapCodeMapping.put("pgMasterRawMaterial", "MRMS");
		mapCodeMapping.put("pgMasterFinishedProduct","MPS");
		mapCodeMapping.put("pgPackingMaterial","MATL");
		mapCodeMapping.put("pgRawMaterial","MATL");
		mapCodeMapping.put("Shared Table","CPS");
		mapCodeMapping.put("pgNonGCASPart", "NGCAS");
		mapCodeMapping.put("pgPackingInstructions", "PI");
		mapCodeMapping.put("pgPackingSubassembly","PSUB");
		mapCodeMapping.put("pgProcessStandard", "PROS");
		mapCodeMapping.put("pgApplianceProduct", "APP");
		mapCodeMapping.put("pgAssembledProduct","ASP");
		mapCodeMapping.put("pgFormulatedProduct","FC");
		mapCodeMapping.put("pgQualitySpecification", "QUAL");
		mapCodeMapping.put("pgStackingPattern", "SPS");
		mapCodeMapping.put("pgStandardOperatingProcedure","SOP");
		mapCodeMapping.put("pgSupplierInformationSheet", "SIS");
		mapCodeMapping.put("pgTestMethod", "TM");
		mapCodeMapping.put("Safety And Regulatory Characteristic","SRS");
		mapCodeMapping.put("pgFormulatedMaterial","FMATL");
		mapCodeMapping.put("pgMaterial","MATL");		
		//Added by V4-2013x.4 for Product Data Search - starts
		
		mapCodeMapping.put("Finished Product Part", "FPP");
		
		mapCodeMapping.put("pgConsumerUnitPart", "COP");
		
		mapCodeMapping.put("pgCustomerUnitPart", "CUP");
		
		mapCodeMapping.put("pgInnerPackUnitPart", "IP");
		
		mapCodeMapping.put("pgTransportUnitPart", "TUP");
		
		mapCodeMapping.put("pgMasterConsumerUnitPart", "MCOP");
		
		mapCodeMapping.put("pgMasterCustomerUnitPart", "MCUP");
		
		mapCodeMapping.put("pgMasterInnerPackUnitPart", "MIP");
		
		mapCodeMapping.put("pgAncillaryPackagingMaterialPart", "APMP");
		
		mapCodeMapping.put("pgPromotionalItemPart", "PIP");
		
		//DSM(Sogeti) - Modified for 2015x.2.1 defct 11246 - Starts
		mapCodeMapping.put("Formulation Part", "FOP");
		//DSM(Sogeti) - Modified for 2015x.2.1 defct 11246 - Ends
		
		mapCodeMapping.put("Packaging Material Part", "PMP");
		
		mapCodeMapping.put("Raw Material", "RMP");
		
		mapCodeMapping.put("pgMasterPackagingMaterialPart", "MPMP");
		
		mapCodeMapping.put("Packaging Assembly Part", "PAP");
		
		mapCodeMapping.put("pgMasterPackagingAssemblyPart", "MPAP");
		
		mapCodeMapping.put("pgPackingInstructions", "PI");
		
		//Modified by V4-2013x.4 for Defect-841 - Starts
		//mapCodeMapping.put("pgAuthorizedTemporaryStandard", "ATS");
		mapCodeMapping.put("pgAuthorizedTemporarySpecification", "ATS");
		//Modified by V4-2013x.4 for Defect-841 - Ends
		
		mapCodeMapping.put("pgStackingPattern", "SPS");
		
		mapCodeMapping.put("pgPPMConstituent", "CNT");
		
		//Added by V4-2013x.4 for Defect-841 - Starts
		mapCodeMapping.put("pgOnlinePrintingPart", "OPP");
		//Added by V4-2013x.4 for Defect-841 - Ends
		
		/*DSM (DS) 2015x.1 - Fix for  - Excel Export of Search results show incorrect Type for Fabricated Part - START*/
		mapCodeMapping.put("pgFabricatedPart","FAB");
		/*DSM (DS) 2015x.1 - Fix for  - Excel Export of Search results show incorrect Type for Fabricated Part - END*/		
		//Added by V4-2013x.4 for Product Data Search - Ends
		
		//Added by DSM (Sogeti) - For 2015x.2 Search Requirement 10411 - Starts
		mapCodeMapping.put("pgAncillaryRawMaterialPart","ARMP");
		
		mapCodeMapping.put("pgAssembledProductPart","APP");
		
		mapCodeMapping.put("pgAuthorizedConfigurationStandard","ACS");
		
		mapCodeMapping.put("pgDeviceProductPart","DPP");
		
		mapCodeMapping.put("Formula Technical Specification","IAPS");
		
		mapCodeMapping.put("pgMasterProductPart","MPP");
		
		mapCodeMapping.put("pgMasterRawMaterialPart","MRMP");
		mapCodeMapping.put("Formulation Process","FOPR");
		//Added by DSM (Sogeti) - For 2015x.2 Search Requirement 10411 - Ends
		
		String strType="";
		String strRootNode="";
		String strAbbr = "";
		Map object = null;
		String strObjectId="";
		DomainObject newDO=null;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try{
		ContextUtil.pushContext(context);
		isContextPushed = true;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			object = (Map) iterator.next();	
			strType=(String)object.get(DomainConstants.SELECT_TYPE);
			if(strType==null){
				strObjectId=(String)object.get(DomainConstants.SELECT_ID);	
				newDO=new DomainObject(strObjectId);
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
				// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
				//if(FrameworkUtil.hasAccess(context, newDO , "read")!=false){
				strType=(String)newDO.getInfo(context,DomainConstants.SELECT_TYPE);
				//}
				//else {vc.add("No Access");}
				// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
			}
			strRootNode=(String)object.get("Root Node");
			if(strTypeName.equalsIgnoreCase(strType))
			{
				vc.add("");
			}
			else if(!"true".equalsIgnoreCase(strRootNode))
			{
				strAbbr = (String)mapCodeMapping.get(strType);
				if(strAbbr != null)
					vc.add(strAbbr);
				else
					vc.add(strType);
			}
			else{
				vc.add("");
			}
			// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		}
		}catch(Exception e){
		throw e;
		}
		finally{
		if(isContextPushed){
		ContextUtil.popContext(context);
		}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Description in CPNENCEBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getDescriptionColumnData(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		String strDescription="";
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		Map object =null;
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			object = (Map) iterator.next();
			strDescription=(String)object.get(DomainConstants.SELECT_DESCRIPTION);
			vc.add(strDescription);
		}
		return vc;
	}
	/**
	 * This API gives data for UOM Attribute in Bill of Material Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getAttributeUOMColumnData(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strRelID ="";
		String strAttrpgUoM = "";
		String strAttributeUoM="";
		Map object = null;
		DomainRelationship dRelEBOM =null;
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			object = (Map) iterator.next();
			strRelID = (String)object.get(DomainConstants.SELECT_RELATIONSHIP_ID);
			strAttrpgUoM = PropertyUtil.getSchemaProperty("attribute_pgQuantityUnitOfMeasure");
			if(strRelID != null && !"".equalsIgnoreCase(strRelID))
			{
				dRelEBOM = new DomainRelationship(strRelID);
				strAttributeUoM=(String)dRelEBOM.getAttributeValue(context,strAttrpgUoM);
				vc.add(strAttributeUoM);
			}
			else
			{
				vc.add("");
			}
		}
		return vc;
	}
	/**
	 * This API gives data for Finished Product Code Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getFinishedProductCode(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strAttributeFPC = PropertyUtil.getSchemaProperty("attribute_pgFinishedProductCode");
		String strObjectID = "";
		String strFPC ="";
		String strRootNode="";
		Map mapObject = null;
		DomainObject dObjFP = null;
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			mapObject = (Map) iterator.next();
			strObjectID = (String)mapObject.get(DomainConstants.SELECT_ID);
			dObjFP = new DomainObject(strObjectID);
			strFPC =(String)dObjFP.getAttributeValue(context, strAttributeFPC);
			strRootNode=(String)mapObject.get("Root Node");
			if(!"true".equalsIgnoreCase(strRootNode))
			{
				vc.add(strFPC);
			} else {
				vc.add("");
			}
		}
		return vc;
	}
	/**
	 * This API gives data for Quantity Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getQuantityForFP(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strAttributepgQuantity = PropertyUtil.getSchemaProperty("attribute_pgQuantity");
		String strAttributeQuantity = PropertyUtil.getSchemaProperty("attribute_Quantity");
		String strRelID = "";
		String strpgQuantity ="";
		String strRootNode="";
		Map mapObject = null;
		DomainRelationship dRelEBOM =null;
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			mapObject = (Map) iterator.next();
			String strType = (String)mapObject.get("type");
			strRelID = (String)mapObject.get(DomainConstants.SELECT_RELATIONSHIP_ID);
			// DSO - Bug Fix -START
			if (UIUtil.isNotNullAndNotEmpty(strRelID))
			{
				// DSO - Bug Fix -END
				dRelEBOM = new DomainRelationship(strRelID);
				strpgQuantity =(String)dRelEBOM.getAttributeValue(context, strAttributepgQuantity);
				strRootNode=(String)mapObject.get("Root Node");
				if(!"true".equalsIgnoreCase(strRootNode))
				{
					vc.add(strpgQuantity);
				}else{
					vc.add("");
				}
			}
			else
			{
				vc.add("");
			}
		}
		return vc;
	}
	/**
	 * Updates the Quantity Attribute on EBOM Relationship.
	 * @param context the eMatrix <code>Context</code> object
	 * @param args holds a HashMap containing the following entries:
	 * paramMap - a HashMap containing String values for "relId" and "New Value".
	 * @return Boolean - true if the operation is successful.
	 * @throws Exception if operation fails
	 * @since EngineeringCentral 10.6 - Copyright (c) 2004, MatrixOne, Inc.
	 */
	public Boolean updateQuantityForFP(Context context, String[] args) throws Exception
	{
		HashMap programMap = (HashMap)JPO.unpackArgs(args);
		HashMap paramMap = (HashMap)programMap.get("paramMap");
		String relId  = (String)paramMap.get("relId");
		if (UIUtil.isNotNullAndNotEmpty(relId)){
			String newQtyValue = (String)paramMap.get("New Value");
			String strAttributepgQuantity = PropertyUtil.getSchemaProperty("attribute_pgQuantity");
			DomainRelationship domRel = new DomainRelationship(relId);
			domRel.setAttributeValue(context, strAttributepgQuantity, newQtyValue);
		}
		return Boolean.valueOf(true);
	}
	/**
	 * This API gives data for Finished Product comment Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getCommentColumnDataFinishedProduct(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		try {
			Map paramMap=(Map)JPO.unpackArgs(args);
			MapList objectList = (MapList) paramMap.get("objectList");
			HashMap paramListMap = (HashMap)paramMap.get("paramList");
			String strAttributeComment = PropertyUtil.getSchemaProperty("attribute_Comment");
			StringList slListSelect = new StringList(1);
			slListSelect.addElement("attribute["+strAttributeComment+"]");
			String objIdArray[] = new String[objectList.size()];
			String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
			//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
			ContextUtil.pushContext(context);
			isContextPushed = true;
			//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			if(objectList != null)
			{
				String strIsRootNode = "";
				String strConnectionID = "";
				String strObjectComment="";
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// String strLevel="";
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				String strObjectType="";
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// int intLevel=0;
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				String strObjectID="";
				DomainObject dobjObjectForComment=null;
				Map mapAttributeData=null;
				for(int i=0; i<objectList.size(); i++)
				{
					objIdArray[i] = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
				}
				BusinessObjectWithSelectList attributeSelectList = null;
				attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
				for (int i = 0; i < objectList.size(); i++)
				{
					strIsRootNode = (String)((Map)objectList.get(i)).get("Root Node");
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
					// strLevel= (String)((Map)objectList.get(i)).get("level");
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
					strObjectType=(String)((Map)objectList.get(i)).get("type");
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
					// intLevel = Integer.parseInt(strLevel);
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
					strObjectID = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
					//Create DomainObject to get required data and get attribute map
					dobjObjectForComment = new DomainObject(strObjectID);
					if(FrameworkUtil.hasAccess(context, dobjObjectForComment , "read")!=false){

						mapAttributeData = (Map)dobjObjectForComment.getAttributeMap(context);
						strObjectComment = (String)mapAttributeData.get(strAttributeComment);
					}
					if(!"true".equalsIgnoreCase(strIsRootNode))
					{
						strConnectionID = (String)((Map)objectList.get(i)).get("id[connection]");
						DomainRelationship domrelEBOM = new DomainRelationship(strConnectionID);
						String strValue = (String)domrelEBOM.getAttributeValue(context, strAttributeComment);
						if(strtypePGPhase.equalsIgnoreCase(strObjectType))
						{
							// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
							// Removed if condition to remove the level check
							//if(intLevel == 1)
							//{
							// Modified by P&G IPM V2/V3 Team for Defect #511 (11) for Release 2013x.4 Start
							//vc.addElement(strObjectComment);
							vc.addElement(strValue);
							// Modified by P&G IPM V2/V3 Team for Defect #511 (11) for Release 2013x.4 End
							/*}
							else
							{
								vc.addElement(strValue);
							}*/
							// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
						}
						else{
							vc.addElement(strValue);
						}
					} else {
						vc.add("");
					}
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		finally {
			if(isContextPushed){
				ContextUtil.popContext(context);
			}
		}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	
	// DSO Fix for 4329 -  BOM Table Comments column data is missing START
	// Copying getCommentColumnDataFinishedProduct as it shared by other tables. Adding changes specific to DSO.
	/**
	 * This API gives data for Finished Product comment Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getCommentColumnDataFinishedProductForDSO(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		try {
			Map paramMap=(Map)JPO.unpackArgs(args);
			MapList objectList = (MapList) paramMap.get("objectList");
			HashMap paramListMap = (HashMap)paramMap.get("paramList");
			String strAttributeComment = PropertyUtil.getSchemaProperty("attribute_Comment");
			StringList slListSelect = new StringList(1);
			slListSelect.addElement("attribute["+strAttributeComment+"]");
			String objIdArray[] = new String[objectList.size()];
			String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
			if(objectList != null)
			{
				String strIsRootNode = "";
				String strConnectionID = "";
				String strObjectComment="";
				String strLevel="";
				String strObjectType="";
				int intLevel=0;
				String strObjectID="";
				DomainObject dobjObjectForComment=null;
				Map mapAttributeData=null;
				for(int i=0; i<objectList.size(); i++)
				{
					objIdArray[i] = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
				}
				BusinessObjectWithSelectList attributeSelectList = null;
				attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
				for (int i = 0; i < objectList.size(); i++)
				{
					strIsRootNode = (String)((Map)objectList.get(i)).get("Root Node");
					strLevel= (String)((Map)objectList.get(i)).get("level");
					strObjectType=(String)((Map)objectList.get(i)).get("type");
					intLevel = Integer.parseInt(strLevel);
					strObjectID = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
					//Create DomainObject to get required data and get attribute map
					dobjObjectForComment = new DomainObject(strObjectID);
					//DSM 2015x.1- HIR Formula Card changes - START
					if(FrameworkUtil.hasAccess(context, dobjObjectForComment , "read")!=false){
					mapAttributeData = (Map)dobjObjectForComment.getAttributeMap(context);
					strObjectComment = (String)mapAttributeData.get(strAttributeComment);
					}
					//DSM 2015x.1 -HIR Formula Card changes - END
					//DSO 2013x.4-  Added the code for comment field of child Product Data  : Start
					//if(null != strIsRootNode && !"true".equalsIgnoreCase(strIsRootNode))
					if (UIUtil.isNullOrEmpty(strIsRootNode) && !"true".equalsIgnoreCase(strIsRootNode))
					  //DSO 2013x.4-  Added the code for comment field of child Product Data  : End
					{
						strConnectionID = (String)((Map)objectList.get(i)).get("id[connection]");
						//DSO 2013x.4-  Added the code for comment field of child Product Data  : Start
						if (UIUtil.isNotNullAndNotEmpty(strConnectionID))
						{
						//DSO 2013x.4-  Added the code for comment field of child Product Data  : End
	                        DomainRelationship domrelEBOM = new DomainRelationship(strConnectionID);
	                        String strValue = (String)domrelEBOM.getAttributeValue(context, strAttributeComment);
	                        if(strtypePGPhase.equalsIgnoreCase(strObjectType))
	                        {
	                            if(intLevel == 1)
	                            {
	                                vc.addElement(strObjectComment);
	                            }
	                            else
	                            {
	                                vc.addElement(strValue);
	                            }
	                        }
	                        else{
	                            vc.addElement(strValue);
	                        }						
						}
						//DSO 2013x.4-  Added the code for comment field of child Product Data  : Start
						else
						{
							vc.addElement("");
						}
						//DSO 2013x.4-  Added the code for comment field of child Product Data  : End
					} else {
						vc.add("");
					}
				}
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return vc;
	}
	// DSO Fix for 4329 -  BOM Table Comments column data is missing END
	/**
	 * This API gives data for Finished Product comment Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getSAPDescriptionForFinishedProduct(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		
		//Platform Search 2018x.0 : Fix for ALM 23865 : Start 
		Map<?, ?> objMap = null;
		StringList sNoAccessObjList = new StringList();
		String strReadAccess = "";
		String strObjId = "";
		for(int i=0; i<objectList.size(); i++)
		{
			objMap = (Map<?, ?>) objectList.get(i);
			strReadAccess = (String) objMap.get("objReadAccess");
			strObjId = (String) objMap.get(DomainConstants.SELECT_ID);
			if("#DENIED!".equals(strReadAccess)) {
				sNoAccessObjList.add(strObjId);
			}
		}
		//Platform Search 2018x.0 : Fix for ALM 23865 : End 
		
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strAttributeTitle = PropertyUtil.getSchemaProperty("attribute_Title");
		// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
		// String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
		StringList slListSelect = new StringList(1);
		slListSelect.addElement("attribute["+strAttributeTitle+"]");
		// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
		/*slListSelect.addElement(DomainConstants.SELECT_TYPE);
		slListSelect.addElement(DomainConstants.SELECT_DESCRIPTION);*/
		// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
		String objIdArray[] = new String[objectList.size()];
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try{
		ContextUtil.pushContext(context);
		isContextPushed = true;		
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		if(objectList != null && !objectList.isEmpty())
		{
			String strIsRootNode = "";
			String strValue ="";
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
			// String strDescription = "";
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
			for(int i=0; i<objectList.size(); i++)
			{
				objIdArray[i] = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			for (int i = 0; i < objectList.size(); i++)
			{
				strIsRootNode = (String)((Map)objectList.get(i)).get("Root Node");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				/*String strLevel = (String)((Map)objectList.get(i)).get("level");
				int intLevel = Integer.parseInt(strLevel);*/
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				strValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strAttributeTitle+"]");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				/*strDescription = (String)attributeSelectList.getElement(i).getSelectData(DomainConstants.SELECT_DESCRIPTION);
				String strType = (String)attributeSelectList.getElement(i).getSelectData(DomainConstants.SELECT_TYPE);
				if(strtypePGPhase.equalsIgnoreCase(strType) && intLevel!=1)
				{
					strValue = strDescription;
				}*/
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
				// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
				/*if("#DENIED!".equalsIgnoreCase(strValue))
				{
					vc.addElement("No Access");
				}*/
				// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
				//else if(!"true".equalsIgnoreCase(strIsRootNode))
				
				//Platform Search 2018x.0 : Fix for ALM 23865 : Start 
				strObjId = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
				if(sNoAccessObjList.contains(strObjId)) {
					vc.add("No Access");
				} else {
					if(!"true".equalsIgnoreCase(strIsRootNode))
					{
						vc.addElement(strValue);
					}else  {
						vc.add("");
					}
				}
				//Platform Search 2018x.0 : Fix for ALM 23865 : End 
			}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		}catch(Exception e){
		throw e;
		}finally{
		if(isContextPushed){
		ContextUtil.popContext(context);
		}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Quantity Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getSAPBOMQuantityForFP(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strAttributepgQuantity = PropertyUtil.getSchemaProperty("attribute_pgCalcQuantity");
		String strRelID = "";
		String strpgQuantity ="";
		String strRootNode="";
		Map mapObject = null;
		DomainRelationship dRelEBOM=null;		
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			mapObject = (Map) iterator.next();
			strRelID = (String)mapObject.get(DomainConstants.SELECT_RELATIONSHIP_ID);
			if (strRelID != null || "".equalsIgnoreCase(strRelID))
			{
				dRelEBOM = new DomainRelationship(strRelID);
				strpgQuantity =(String)dRelEBOM.getAttributeValue(context, strAttributepgQuantity);
				strRootNode=(String)mapObject.get("Root Node");
				if(!"true".equalsIgnoreCase(strRootNode))
				{
					vc.add(strpgQuantity);
				}
			}
			else
			{
				vc.add("");
			}
		}
		return vc;
	}
	/**
	 * This API gives data for Max column in Formulated Product table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getMaxColumnValue(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strMax = PropertyUtil.getSchemaProperty("attribute_pgMaxQuantity");
		String strpgMax = PropertyUtil.getSchemaProperty("attribute_pgMaxCalcQuantity");
		String strTypepgPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strIsRootNode = null;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try {
			ContextUtil.pushContext(context);
			isContextPushed = true;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			if(objectList != null)
			{	
				int intLevel =0;
				String strObjectID = "";
				String strConnectionID = "";
				String strLevel="";
				String strObjectType="";
				String strMaxValue = "";
				Map mapObjectData =null;
				Attribute attrpgMax = null;
				DomainObject domObjData=null;
				Relationship relEBOM = null;
				for(Iterator itr = objectList.iterator();itr.hasNext();)
				{
					mapObjectData = (Map)itr.next();
					strObjectID = (String)mapObjectData.get(DomainConstants.SELECT_ID);
					strConnectionID = (String)mapObjectData.get("id[connection]");
					strLevel= (String)mapObjectData.get("level");
					intLevel = Integer.parseInt(strLevel);
					strIsRootNode = (String)mapObjectData.get("Root Node");
					domObjData = new DomainObject(strObjectID);
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
					//if(FrameworkUtil.hasAccess(context, domObjData , "read")!=false){
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
					strObjectType = (String)domObjData.getInfo(context,DomainConstants.SELECT_TYPE);
					if(intLevel >= 1 && !"true".equalsIgnoreCase(strIsRootNode))
					{
						if(strTypepgPhase.equalsIgnoreCase(strObjectType) && intLevel == 1)
						{
							vc.addElement("");
						} else {
							relEBOM = new Relationship(strConnectionID);
							attrpgMax = (Attribute)relEBOM.getAttributeValues(context,strMax);
							strMaxValue = (String)attrpgMax.getValue();
							vc.addElement(strMaxValue);
						}
					}
					else
					{
						vc.addElement("");
					}
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
				//}
				//else {vc.addElement("No Access");}
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
				}
			}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (isContextPushed){
				ContextUtil.popContext(context);
			}
		}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Min column in Formulated Product table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getMinColumnValue(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strMin = PropertyUtil.getSchemaProperty("attribute_pgMinQuantity");
		String strpgMin = PropertyUtil.getSchemaProperty("attribute_pgMinCalcQuantity");
		String strTypepgPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strIsRootNode = null;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try {
			ContextUtil.pushContext(context);
			isContextPushed = true;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			if(objectList != null)
			{		
				int intLevel =0;
				String strObjectID = "";
				String strConnectionID = "";
				String strLevel= "";
				String strObjectType ="";
				String strMinValue = "";
				Map mapObjectData = null;
				Attribute attrpgMin = null;
				DomainObject domObjData = null;
				Relationship relEBOM = null;
				for(Iterator itr = objectList.iterator();itr.hasNext();)
				{
					mapObjectData = (Map)itr.next();
					strObjectID = (String)mapObjectData.get(DomainConstants.SELECT_ID);
					strConnectionID = (String)mapObjectData.get("id[connection]");
					strLevel= (String)mapObjectData.get("level");
					intLevel = Integer.parseInt(strLevel);
					strIsRootNode = (String)mapObjectData.get("Root Node");
					domObjData = new DomainObject(strObjectID);
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
					//if(FrameworkUtil.hasAccess(context, domObjData , "read")!=false){
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
					strObjectType = (String)domObjData.getInfo(context,DomainConstants.SELECT_TYPE);
					if(intLevel >= 1 && !"true".equalsIgnoreCase(strIsRootNode))
					{
						if(strTypepgPhase.equalsIgnoreCase(strObjectType) && intLevel == 1)
						{
							vc.addElement("");
						} else {
							relEBOM = new Relationship(strConnectionID);
							attrpgMin = (Attribute)relEBOM.getAttributeValues(context,strMin);
							strMinValue = (String)attrpgMin.getValue();
							vc.addElement(strMinValue);
						}
					}
					else
					{
						vc.addElement("");
					}
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
					//}else {vc.addElement("No Access");}
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
				}
			}
			//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (isContextPushed){
				ContextUtil.popContext(context);
			}
		}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Target column in Formulated Product table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getTargetColumnValue(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String strpgTarget = PropertyUtil.getSchemaProperty("attribute_pgQuantity");
		String strTypepgPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strIsRootNode = null;
		String strTypepgBSF = PropertyUtil.getSchemaProperty("type_pgBaseFormula");
		String strAttrpgIngredientLoss = PropertyUtil.getSchemaProperty("attribute_pgIngredientLoss");
		String strAttrpgIngredientLossUoM = PropertyUtil.getSchemaProperty("attribute_pgIngredientLossUnitOfMeasure");
		String strAttrpgSubTotal = PropertyUtil.getSchemaProperty("attribute_pgSubTotal");
		String strAttrpgSubTotalUoM = PropertyUtil.getSchemaProperty("attribute_pgSubTotalUnitOfMeasure");
		String strAttrpgTotalUoM = PropertyUtil.getSchemaProperty("attribute_pgQuantityUnitOfMeasure");
		String strAttrpgIngredientLossComment = PropertyUtil.getSchemaProperty("attribute_pgIngredientLossComment");
		String strAttrpgTotalBSF = PropertyUtil.getSchemaProperty("attribute_pgTotal");
		String strpgIngredientLoss = "";
		String strpgIngredientLossUoM = "";
		String strIngredientLossComment = "";
		String strSubTotal = "";
		String strSubTotalUoM = "";
		String strTotal = "";
		String strTotalUoM = "";
		//Modified for defect 4847 - Export to excel issue
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strReportFormat = (String)paramListMap.get("reportFormat");
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try {
			ContextUtil.pushContext(context);
			isContextPushed = true;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			if(objectList != null)
			{					
				int intLevel =0;
				String strObjectID = "";
				String strConnectionID = "";
				String strLevel="";
				String strObjectType="";
				String strMinValue = "";
				Map mapObjectData =null;
				Map mapTestData=null;
				Attribute attrpgMin = null;
				DomainObject domObjData=null;
				Relationship relEBOM = null;
				for(Iterator itr = objectList.iterator();itr.hasNext();)
				{
					mapObjectData = (Map)itr.next();
					strObjectID = (String)mapObjectData.get(DomainConstants.SELECT_ID);
					strConnectionID = (String)mapObjectData.get("id[connection]");
					strLevel= (String)mapObjectData.get("level");
					intLevel = Integer.parseInt(strLevel);
					strIsRootNode = (String)mapObjectData.get("Root Node");
					domObjData = new DomainObject(strObjectID);
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
					//if(FrameworkUtil.hasAccess(context, domObjData , "read")!=false){
					// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
					strObjectType = (String)domObjData.getInfo(context,DomainConstants.SELECT_TYPE);
					mapTestData = domObjData.getAttributeMap(context);
					if(intLevel >= 1 && !"true".equalsIgnoreCase(strIsRootNode))
					{
						// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
						// Modified if condition to remove the level check
						// Uncommented for Defect fix 7537 by DSM(Sogeti) 2015x.2 Starts
						 if(strTypepgPhase.equalsIgnoreCase(strObjectType) && intLevel == 1)
						// Uncommented for Defect fix 7537 by DSM(Sogeti) 2015x.2 Ends
						// Commented for Defect fix 7537 by DSM(Sogeti) 2015x.2 Starts
						//if(strTypepgPhase.equalsIgnoreCase(strObjectType))
						// Commented for Defect fix 7537 by DSM(Sogeti) 2015x.2 Ends
						// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
						{
							//Get Ingredient Loss data
							strpgIngredientLoss = (String)mapTestData.get(strAttrpgIngredientLoss);
							strpgIngredientLossUoM = (String)mapTestData.get(strAttrpgIngredientLossUoM);
							//Get Subtotal data
							strSubTotal = (String)mapTestData.get(strAttrpgSubTotal);
							strSubTotalUoM = (String)mapTestData.get(strAttrpgSubTotalUoM);
							//Get Total data
							strTotal = (String)mapTestData.get(strpgTarget);
							strTotalUoM = (String)mapTestData.get(strAttrpgTotalUoM);
							//Modified for defect 4847 - Export to excel issue
							if(strReportFormat != null && strReportFormat.length()>0)
							{
								vc.add("Subtotal: "+strSubTotal+" "+strSubTotalUoM+"\nIngredient Loss: "+strpgIngredientLoss+" "+strpgIngredientLossUoM+"\nTotal: "+strTotal+" "+strTotalUoM);
							}
							else
							{	
								//Modified by DSM(Sogeti)2015x.2 to add line feed after Ingredient Loss for defect 7488 on 26-10-2016-Start
								vc.add("<b>Subtotal:</b> "+strSubTotal+" "+strSubTotalUoM+"<br/><b>Ingredient Loss:</b> "+strpgIngredientLoss+" "+strpgIngredientLossUoM+"<br/><b>Total:</b> "+strTotal+" "+strTotalUoM);
								//Modified by DSM(Sogeti)2015x.2 to add line feed after Ingredient Loss for defect 7488 on 26-10-2016-Ends
							}
						} else {
							relEBOM = new Relationship(strConnectionID);
							attrpgMin = (Attribute)relEBOM.getAttributeValues(context,strpgTarget);
							strMinValue = (String)attrpgMin.getValue();
							vc.addElement(strMinValue);
						}
					}
					else
					{
						if("true".equalsIgnoreCase(strIsRootNode) && strObjectType.equalsIgnoreCase(strTypepgBSF))
						{
							//Get Ingredient Loss data
							strpgIngredientLoss = (String)mapTestData.get(strAttrpgIngredientLoss);
							strpgIngredientLossUoM = (String)mapTestData.get(strAttrpgIngredientLossUoM);
							//Get Subtotal data
							strSubTotal = (String)mapTestData.get(strAttrpgSubTotal);
							strSubTotalUoM = (String)mapTestData.get(strAttrpgSubTotalUoM);
							//Get Total data
							strTotal = (String)mapTestData.get(strAttrpgTotalBSF);
							strTotalUoM = (String)mapTestData.get(strAttrpgTotalUoM);
							//Modified for defect 4847 - Export to excel issue
							if(strReportFormat != null && strReportFormat.length()>0)
							{
								vc.add("Subtotal: "+strSubTotal+" "+strSubTotalUoM+"\nIngredient Loss: "+strpgIngredientLoss+" "+strpgIngredientLossUoM+"\nTotal: "+strTotal+" "+strTotalUoM);
							}
							else
							{
								//Modified by DSM(Sogeti)2015x.2 to add line feed after Ingredient Loss for defect 7488 on 26-10-2016-Starts
								vc.add("<b>Subtotal:</b> "+strSubTotal+" "+strSubTotalUoM+"<br/><b>Ingredient Loss:</b> "+strpgIngredientLoss+" "+strpgIngredientLossUoM+"<br/><b>Total:</b> "+strTotal+" "+strTotalUoM);
								//Modified by DSM(Sogeti)2015x.2 to add line feed after Ingredient Loss for defect 7488 on 26-10-2016-Ends
							}
						}
						else
						{
							vc.addElement("");
						}
					}
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
				//}else {vc.addElement("No Access");}
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
				}
			}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (isContextPushed){
				ContextUtil.popContext(context);
			}
		}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data of SAP Type for SAP BOM view
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getSAPTypeForSAPBOMView(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strAttributeSAPType = PropertyUtil.getSchemaProperty("attribute_pgSAPType");
		StringList slListSelect = new StringList(1);
		slListSelect.addElement("attribute["+strAttributeSAPType+"]");
		String objIdArray[] = new String[objectList.size()];
		if(objectList != null)
		{
			String strIsRootNode = null;
			for(int i=0; i<objectList.size(); i++)
			{
				objIdArray[i] = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			String strValue ="";
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			for (int i = 0; i < objectList.size(); i++)
			{
				strIsRootNode = (String)((HashMap)objectList.get(i)).get("Root Node");
				strValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strAttributeSAPType+"]");
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					vc.addElement(strValue);
				} else  {
					vc.add("");
				}
			}
		}
		return vc;
	}
	/**
	 * This API gives data of base unit of measure for SAP BOM view
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getBUOMForSAPBOMView(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strAttributeBUOM = PropertyUtil.getSchemaProperty("attribute_pgBaseUnitOfMeasure");
		StringList slListSelect = new StringList(1);
		slListSelect.addElement("attribute["+strAttributeBUOM+"]");
		String objIdArray[] = new String[objectList.size()];
		if(objectList != null)
		{
			String strIsRootNode = "";
			for(int i=0; i<objectList.size(); i++)
			{
				objIdArray[i] = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			String strValue="";
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			for (int i = 0; i < objectList.size(); i++)
			{
				strIsRootNode = (String)((HashMap)objectList.get(i)).get("Root Node");
				strValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strAttributeBUOM+"]");
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					vc.addElement(strValue);
				} else  {
					vc.add("");
				}
			}
		}
		return vc;
	}
	/**
	 * This API gives state for SAP BOM View
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getStateColumnData(Context context,String[]args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		//String strAttributeBUOM = PropertyUtil.getSchemaProperty("attribute_pgBaseUnitOfMeasure");
		StringList slListSelect = new StringList(1);
		slListSelect.addElement(DomainConstants.SELECT_CURRENT);
		String objIdArray[] = new String[objectList.size()];
		if(objectList != null)
		{
			String strIsRootNode = "";
			for(int i=0; i<objectList.size(); i++)
			{
				objIdArray[i] = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			String strValue="";
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			for (int i = 0; i < objectList.size(); i++)
			{
				strIsRootNode = (String)((HashMap)objectList.get(i)).get("Root Node");
				strValue = (String)attributeSelectList.getElement(i).getSelectData(DomainConstants.SELECT_CURRENT);
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					vc.addElement(strValue);
				} else  {
					vc.add("");
				}
			}
		}
		return vc;
	}
	/**
	 * This API gives Effectivity Date for SAP BOM View
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getEffectivityDateColumnData(Context context,String[]args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strAttributeEffectivityDate = PropertyUtil.getSchemaProperty("attribute_EffectivityDate");
		StringList slListSelect = new StringList(1);
		slListSelect.addElement("attribute["+strAttributeEffectivityDate+"]");
		String objIdArray[] = new String[objectList.size()];
		if(objectList != null)
		{
			String strIsRootNode = null;
			for(int i=0; i<objectList.size(); i++)
			{
				objIdArray[i] = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			for (int i = 0; i < objectList.size(); i++)
			{
				strIsRootNode = (String)((HashMap)objectList.get(i)).get("Root Node");
				String strValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strAttributeEffectivityDate+"]");
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					vc.addElement(strValue);
				} else  {
					vc.add("");
				}
			}
		}
		return vc;
	}
	/**
	 * This API gives highest approved revision for SAP BOM View
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getHARColumnData(Context context,String[]args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strObjectWhere = "(current==Release) && (revision==last || next.current!=Release))";
		StringList objectSelects = new StringList(1);
		objectSelects.add(DomainConstants.SELECT_REVISION);
		if(objectList != null)
		{
			String strIsRootNode = null;
			String strObjectType = null;
			String strObjectName = null;
			String strHighestRevision = "";
			for (int i = 0; i < objectList.size(); i++)
			{
				Map mapObjectData = (Map)objectList.get(i);	
				strIsRootNode = (String)mapObjectData.get("Root Node");
				//Get the highest released revision
				strObjectType = (String)mapObjectData.get(DomainConstants.SELECT_TYPE);
				strObjectName = (String)mapObjectData.get(DomainConstants.SELECT_NAME);
				MapList mlRevisionData = DomainObject.findObjects(
						context,
						strObjectType,
						strObjectName,
						"*",
						"*",
						"*",
						strObjectWhere,
						false,
						objectSelects
				);
				if(mlRevisionData.size() > 0)
				{
					strHighestRevision = (String)(((Map)mlRevisionData.get(0)).get(DomainConstants.SELECT_REVISION));
				}
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					vc.addElement(strHighestRevision);
				} else  {
					vc.add("");
				}
			}
		}
		return vc;
	}
	/**
	 * This API gives Abbreviation for Specification SubType column in BOM table
	 *
	 * @param context
	 * @param args
	 * @return Vector
	 * @throws Exception
	 */
	public Vector getSpecificationSubtype(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		/*DSO 2013x.4 - Changes to handle attribute and picklist of Specification Sub Type for Substitute view : START*/
		Map mpColumnMap = (HashMap)paramMap.get("columnMap");

		Map mpSettingsMap = (HashMap) mpColumnMap.get("settings");	

		String strColumnName = (String)mpSettingsMap.get("Column Name");
		/*DSO 2013x.4 - Changes to handle attribute and picklist of Specification Sub Type for Substitute view : END*/
		Map mapDataMapping = new HashMap();
		mapDataMapping.put("pgPackingMaterial","Packaging");
		mapDataMapping.put("pgRawMaterial","Raw");
		String strAttributeName = PropertyUtil.getSchemaProperty("attribute_pgAssemblyType");
		String strAttributeCSSType = PropertyUtil.getSchemaProperty("attribute_pgCSSType");
		String strTypepgFinishedProduct = PropertyUtil.getSchemaProperty("type_pgFinishedProduct");
		String strTypepgQualitySpecification = PropertyUtil.getSchemaProperty("type_pgQualitySpecification");
		String strAttrPgAssemblyType = "";
		String strTypepgTestMethod= PropertyUtil.getSchemaProperty("type_pgTestMethod");
		String strAttrPgCSSType = "";
		String strID = "";
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		//DSO 2013x.4 Added check to handle Specification SubType picklist : START
		String strAssemblyTypeRel = PropertyUtil.getSchemaProperty(context, "relationship_pgPDTemplatestopgPLIAssemblyType");
		String strOriginatingSource = PropertyUtil.getSchemaProperty(context, "attribute_pgOriginatingSource");
		//DSO 2013x.4 Added check to handle Specification SubType picklist : END
		Map mapObject = null;
		String strType="";
		String strRootNode="";
		String strSpecSubType = "";
		/*DSO 2013x.4 - Changes to handle attribute and picklist of Specification Sub Type for Substitute view : START*/
		String strPicklistValue = DomainConstants.EMPTY_STRING;
		/*DSO 2013x.4 - Changes to handle attribute and picklist of Specification Sub Type for Substitute view : END*/
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - START
		String sEBOMSubsRelid = DomainConstants.EMPTY_STRING;
		String[] arrRelIdArray = null;
		MapList mlresultList = null;
		Iterator itrEBOM = null;
		Map mpEBOMMap = null;
		StringList slRelSelStmt = new StringList(2);
		slRelSelStmt.add(BusinessUtil.strcat("to.",DomainObject.getAttributeSelect(${CLASS:pgDSOConstants}.ATTR_PG_ASSEMBLY_TYPE)));
		slRelSelStmt.add(BusinessUtil.strcat("to.id"));
		//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - END
		try{
			ContextUtil.pushContext(context);
			isContextPushed = true;
			// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
			{
				mapObject = (Map) iterator.next();
				//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - START
				//strType=(String)mapObject.get(DomainConstants.SELECT_TYPE);
				strType=(String)mapObject.get("SubstituteType");
				//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - ENDS

				strID = (String)mapObject.get(DomainConstants.SELECT_ID);	
				DomainObject dom = new DomainObject(strID);
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
				// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
				//if(FrameworkUtil.hasAccess(context, dom , "read")!=false){
				// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
				// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
				if(strType==null){
					strType=(String)dom.getInfo(context,DomainConstants.SELECT_TYPE);
				}
				//DSO 2013x.4 Added check to handle Specification SubType picklist : START
				//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - START
				//if(UTILS.isOfDSOOrigin(context, strID)){
				//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - END
				/*DSO 2013x.4 - Changes to handle attribute and picklist of Specification Sub Type for Substitute view : START*/
				strPicklistValue = dom.getInfo(context, "attribute[" + ${CLASS:pgDSOConstants}.ATTR_PG_ASSEMBLY_TYPE+ "]");
				if("DSOSubstituteForSpecificationSubType".equalsIgnoreCase(strColumnName)){
					//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - START
					//DSM(DS) 2018x.0 - ALM 24083 - Invalid Characters in GenDoc and All info view - START
					if(UIUtil.isNotNullAndNotEmpty((String) mapObject.get("EBOM ID"))){
					//DSM(DS) 2018x.0 - ALM 24083 - Invalid Characters in GenDoc and All info view - END
					sEBOMSubsRelid        = (String)mapObject.get("EBOM ID");
					arrRelIdArray          = new String[1];
					arrRelIdArray[0]          = sEBOMSubsRelid;
					mlresultList           = DomainRelationship.getInfo(context,arrRelIdArray,slRelSelStmt);
					itrEBOM                 = mlresultList.iterator();
					mpEBOMMap = (Map) itrEBOM.next();
					strPicklistValue        =(String)mpEBOMMap.get(BusinessUtil.strcat("to.",DomainObject.getAttributeSelect(${CLASS:pgDSOConstants}.ATTR_PG_ASSEMBLY_TYPE)));
					strID = (String)mpEBOMMap.get("to.id");
					dom.setId(strID);
					}
				    //DSM(DS) 2018x.0 - ALM 24083 - Invalid Characters in GenDoc and All info view - START
					else {
						strPicklistValue = dom.getInfo(context, "from["+DomainConstants.RELATIONSHIP_EBOM+"].to.attribute[" + ${CLASS:pgDSOConstants}.ATTR_PG_ASSEMBLY_TYPE+ "]");
					}
					//DSM(DS) 2018x.0 - ALM 24083 - Invalid Characters in GenDoc and All info view - END
					//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - END

					//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - START
				}
				if(UTILS.isOfDSOOrigin(context, strID)){
					//DSM(DS) 2018x.0 - Fix for ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - END
					//}

					if(BusinessUtil.isNullOrEmpty(strPicklistValue)){
						strPicklistValue = "";
					}
					mapDataMapping.put(strType, strPicklistValue);
					/*DSO 2013x.4 - Changes to handle attribute and picklist of Specification Sub Type for Substitute view : END*/

				} else {
			//DSO 2013x.4 Added check to handle Specification SubType picklist : END				
			if (strTypepgFinishedProduct.equals(strType))
			{
				strAttrPgAssemblyType = dom.getInfo(context, //context
						"attribute["+strAttributeName+"]");//java.lang.String select
				//Modified by V4 as per defect 1132 - Starts	
				if("".equals(strAttrPgAssemblyType)){
					mapDataMapping.put("pgFinishedProduct","Finished Product");
				}
				else if("FWIP-Finished Work in Process".equals(strAttrPgAssemblyType)) {
					mapDataMapping.put("pgFinishedProduct","FWIP-Finished Work in Process");			
				}
				/*else if("FWIP-Finished Work in Process".equals(strAttrPgAssemblyType)) {
				 mapDataMapping.put("pgFinishedProduct","FWIP");			
				}*/
				//Modified by V4 as per defect 1132 - Ends
				else if("Purchased Subassembly".equals(strAttrPgAssemblyType)){
					mapDataMapping.put("pgFinishedProduct","Purchased Subassembly");	
				}
				else if("Purchased and/or Produced Subassembly".equals(strAttrPgAssemblyType)){
					mapDataMapping.put("pgFinishedProduct","Purchased and/or Produced Subassembly");	
				}				
			} else if (strTypepgQualitySpecification.equals(strType)){
				strAttrPgCSSType = dom.getInfo(context,"attribute["+strAttributeCSSType+"]");
				//DSM(Sogeti) - 2015x.2 Added for defect 7790 - Starts
				if(!UIUtil.isNullOrEmpty(strAttrPgCSSType)){
					mapDataMapping.put("pgQualitySpecification",strAttrPgCSSType);
				}
				else{
					strAttrPgAssemblyType=dom.getInfo(context,"attribute["+strAttributeName+"]");
					if(!UIUtil.isNullOrEmpty(strAttrPgAssemblyType)){
						if(strAttrPgAssemblyType.equalsIgnoreCase("Sampling"))
						{
							strSpecSubType = "SMPL";
						} 
						else if(strAttrPgAssemblyType.equalsIgnoreCase("Cleaning/Inspection/Lubrication"))
						{
							strSpecSubType = "CIL";
						}
						mapDataMapping.put("pgQualitySpecification",strSpecSubType);
					}
				}
				//DSM(Sogeti) - 2015x.2 Added for defect 7790 - Ends
			} else if (strTypepgTestMethod.equals(strType))
			{   strAttrPgCSSType = dom.getInfo(context, //context
					"attribute["+strAttributeCSSType+"]");//java.lang.String select
			if ("TAMU".equals(strAttrPgCSSType)){
				mapDataMapping.put("pgTestMethod","TAMU");
			}
			else{
				mapDataMapping.put("pgTestMethod","");
			}
			}
			//P&G: Added for V4 to include pgPackingSubassembly
			else if (pgV3Constants.TYPE_PGPACKINGSUBASSEMBLY.equals(strType))
			{
				strAttrPgAssemblyType = dom.getInfo(context, //context
						"attribute["+strAttributeName+"]");//java.lang.String select
				if("Purchased Subassembly".equals(strAttrPgAssemblyType)){
					mapDataMapping.put("pgPackingSubassembly","Purchased Subassembly");	
				}
				else if("Purchased and/or Produced Subassembly".equals(strAttrPgAssemblyType)){
					mapDataMapping.put("pgPackingSubassembly","Purchased and/or Produced Subassembly");	
				}				
				//DSO 2013x.4 Added check to handle Specfication SubType picklist : START
				}
				//DSO 2013x.4 Added check to handle Specfication SubType picklist : END
			}
			//P&G: Addition by V4 ends
			strRootNode=(String)mapObject.get("Root Node");
			if(!"true".equalsIgnoreCase(strRootNode))
			{
				strSpecSubType = (String)mapDataMapping.get(strType);
				if(strSpecSubType != null)
				{
					vc.add(strSpecSubType);
				}
				else
				{
					vc.add("");
				}
			}else{
				vc.add("");
			}
		// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		//}else {vc.add("No Access");}
		// commented for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
		}
		}// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		catch(Exception e){
		throw e;
		}
		finally{
		if(isContextPushed){
		ContextUtil.popContext(context);
		}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Batch/Unit Size column in BOM table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getBatchUnitSize(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String objIdArray[] = new String[objectList.size()];
		String strTarget = PropertyUtil.getSchemaProperty("attribute_pgBatchUnitSize");
		String strMin = PropertyUtil.getSchemaProperty("attribute_pgMinQuantity");
		String strMax = PropertyUtil.getSchemaProperty("attribute_pgMaxQuantity");
		String strUOM = PropertyUtil.getSchemaProperty("attribute_pgBatchUnitSizeUnitOfMeasure");
		String strpgPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		String strIsRootNode = null;
		StringList slListSelect = new StringList(1);
		slListSelect.addElement("attribute["+strTarget+"]");
		slListSelect.addElement("attribute["+strMin+"]");
		slListSelect.addElement("attribute["+strMax+"]");
		slListSelect.addElement("attribute["+strUOM+"]");
		slListSelect.addElement(DomainConstants.SELECT_TYPE);
		//Modified for defect 4847 - Export to excel issue
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strReportFormat = (String)paramListMap.get("reportFormat");
		if(objectList != null)
		{
			for(int i=0;i<objectList.size();i++)
			{
				objIdArray[i] = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
			// int intLevel =0;
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
			String strType = "";
			String strTargetValue =  "";
			String strMinValue =  "";
			String strMaxValue =  "";
			String strUOMValue =  "";
			for (int i = 0; i < objectList.size(); i++)
			{
				StringBuffer sbFinalValue = new StringBuffer();
				strIsRootNode = (String)((HashMap)objectList.get(i)).get("Root Node");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				/*String strLevel = (String)((HashMap)objectList.get(i)).get("level");
				intLevel = Integer.parseInt(strLevel);*/
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				strType = (String)attributeSelectList.getElement(i).getSelectData(DomainConstants.SELECT_TYPE);
				strTargetValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strTarget+"]");
				strMinValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strMin+"]");
				strMaxValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strMax+"]");
				strUOMValue = (String)attributeSelectList.getElement(i).getSelectData("attribute["+strUOM+"]");
				// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// Modified if condition to remove the level check
				// if(!"true".equalsIgnoreCase(strIsRootNode) && strpgPhase.equalsIgnoreCase(strType) && intLevel <= 1)
				if(!"true".equalsIgnoreCase(strIsRootNode) && strpgPhase.equalsIgnoreCase(strType))
				// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				{
					//Modified for defect 4847 - Export to excel issue
					if(strReportFormat != null && strReportFormat.length()>0)
					{
						sbFinalValue.append("Target: "+strTargetValue);
						sbFinalValue.append("\nRange Min: "+strMinValue+"\n");
						sbFinalValue.append("Range Max: "+strMaxValue);
						sbFinalValue.append("\nUnit of Measure: "+strUOMValue+"\n");
						vc.addElement(sbFinalValue.toString());
					}
					else
					{
						sbFinalValue.append("<b>Target:</b> "+strTargetValue);
						sbFinalValue.append("<br><b>Range Min:</b> "+strMinValue+"</br>");
						sbFinalValue.append("<b>Range Max:</b> "+strMaxValue);
						sbFinalValue.append("<br><b>Unit of Measure:</b> "+strUOMValue+"</br>");
						vc.addElement(sbFinalValue.toString());
					}
				}
				else
				{
					vc.addElement("");
				}
			}
		}
		return vc;
	}
	/**
	 * This API gives data for Include In COS Analysis column in BOM table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getIncludeInCOSAnalysis(Context context,String[] args) throws Exception
	{
		String strRelId ="";
		String strCOSAnalysis ="";
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		String objIdArray[] = new String[objectList.size()];
		Map mapRelData=null;
		Relationship relEBOM = null;
		Attribute attrIncludeCOSAnalysis = null;
		for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
		{
			mapRelData = (Map) iterator.next();
			strRelId = (String)mapRelData.get("id[connection]");
			String strLevel = (String) mapRelData.get("level");
			int intLevel = Integer.parseInt(strLevel);
			DomainObject dObj = new DomainObject((String) mapRelData.get(DomainConstants.SELECT_ID));
			String strObjectType = (String)dObj.getInfo(context,"type");
			if(strRelId != null && !"".equalsIgnoreCase(strRelId) && !(strObjectType.equalsIgnoreCase("pgPhase") && intLevel == 1))
			{
				relEBOM = new Relationship(strRelId);
				attrIncludeCOSAnalysis = (Attribute)relEBOM.getAttributeValues(context,"pgIncludeInCOSAnalysis");
				strCOSAnalysis = (String)attrIncludeCOSAnalysis.getValue();
				if("TRUE".equalsIgnoreCase(strCOSAnalysis))
					vc.addElement("Yes");
				else if("FALSE".equalsIgnoreCase(strCOSAnalysis))
					vc.addElement("No");
				else
					vc.addElement(strCOSAnalysis);
			}else{
				vc.addElement("");
			}
		}
		return vc;
	}
	/**
	 * This API gives Name column data - Replace name of Non GCAS object by "Non GCAS"
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getModifiedName(Context context,String[]args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);		
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strReportFormat = (String)paramListMap.get("reportFormat");
		String strTypeNonGCAS = PropertyUtil.getSchemaProperty("type_pgNonGCASPart");		
		String strTypepgPhase = PropertyUtil.getSchemaProperty("type_pgPhase");		
		StringList slListSelect = new StringList(2);
		slListSelect.addElement(DomainConstants.SELECT_NAME);
		slListSelect.addElement(DomainConstants.SELECT_TYPE);
		String objIdArray[] = new String[objectList.size()];
		String strId="";
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
	try{
			// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
			ContextUtil.pushContext(context);
			isContextPushed = true;
			// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		if(objectList != null)
		{
			String strIsRootNode = "";
			for(int i=0; i<objectList.size(); i++)
			{
				objIdArray[i] = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
			}
			BusinessObjectWithSelectList attributeSelectList = null;
			attributeSelectList = BusinessObject.getSelectBusinessObjectData(context,objIdArray,slListSelect);
			String strName ="";
			String strType ="";
			for (int i = 0; i < objectList.size(); i++)
			{
				StringBuffer sbResult = new StringBuffer();
				StringBuffer sbURL = new StringBuffer(200);
				strName = (String)attributeSelectList.getElement(i).getSelectData(DomainConstants.SELECT_NAME);
				strType = (String)attributeSelectList.getElement(i).getSelectData(DomainConstants.SELECT_TYPE);		
				Map mapObjectlist = (Map)objectList.get(i);
				strId = (String)mapObjectlist.get(DomainConstants.SELECT_ID);
				strIsRootNode = (String)mapObjectlist.get("Root Node");
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					//P&G: Modified by V4 to display connected revision instead of latest revision. Under V4, latest revision will be connected to objects. hence we do not need to use latest revision.
					//strId=getLatestReleasedObjectId(context,strId);
					//P&G: Modification by V4 ends
				}
				if(strTypepgPhase.equalsIgnoreCase(strType)) {
					sbResult.append(strName);
				}	else if((strTypeNonGCAS).equalsIgnoreCase(strType)){
					sbResult.append("Non-GCAS");
				}
				else {					
					if(strReportFormat != null && strReportFormat.length()>0)
					{
						sbResult.append(strName);
					}else{
						sbURL.append("../common/emxTree.jsp?objectId="+strId+"'");				
						sbResult.append("<a href=\"javascript:emxTableColumnLinkClick('"+sbURL.toString()+""); 		
						sbResult.append(",'700");
						sbResult.append("','");
						sbResult.append("600");					
						sbResult.append("','false', '");
						sbResult.append("popup");
						sbResult.append("')");					
						sbResult.append("\"");
						sbResult.append(" title='"+strName+"'>");					
						sbResult.append(strName);
						sbResult.append("</a>");			
					}
				}	
				vc.addElement(sbResult.toString());	
			}
		}
	}
	catch(Exception e){
	throw e;
	}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
	finally{
		if(isContextPushed){
		ContextUtil.popContext(context);
		}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Finished Product Description Attribute in pgCPNENCFinishedProductBOMIndentedSummary  and formulated Product BOM Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getPackingTableAndFILDescription(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String ObjectId="";
		String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try{
		ContextUtil.pushContext(context);
		isContextPushed = true;
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		if(objectList != null && !objectList.isEmpty())
		{
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
			// int intLevel = 0;
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
			String strIsRootNode = "";
			String strValue ="";
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
			// String strLevel= "";
			// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
			String strObjectType="";
			String strObjectID = "";
			String strAttributeValue;
			DomainObject dobjObject =null;
			for (int i = 0; i < objectList.size(); i++)
			{
				strIsRootNode = (String)((Map)objectList.get(i)).get("Root Node");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// strLevel= (String)((Map)objectList.get(i)).get("level");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				strObjectType=(String)((Map)objectList.get(i)).get("type");
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// intLevel = Integer.parseInt(strLevel);
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				strObjectID = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
				if(!"true".equalsIgnoreCase(strIsRootNode))
				{
					// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
					// Modified if condition to remove the level check
					//if(strtypePGPhase.equalsIgnoreCase(strObjectType)&&intLevel==1)
					if(strtypePGPhase.equalsIgnoreCase(strObjectType))
					// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
					{
						dobjObject = new DomainObject(strObjectID);
						// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Starts
						if(FrameworkUtil.hasAccess(context, dobjObject , "read")!=false){
						strAttributeValue = (String)dobjObject.getInfo(context, DomainConstants.SELECT_DESCRIPTION);
						vc.addElement(strAttributeValue);
						}else
						{
							vc.addElement("No Access");
						}
						// Modified for Defect fix 3896 by DSM 2015x.1(Sogeti) Ends
					}
					else{
						vc.addElement("");
					}
				} else {
					vc.add("");
				}
			}
		}
	// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
	}
	catch(Exception e){
	throw e;
	}finally{
		if(isContextPushed){
		ContextUtil.popContext(context);
		}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives data for Finished Product Packing Level Attribute in pgCPNENCFinishedProductBOMIndentedSummary Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getPackingLevelForFinishedProduct(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strAttributeName = PropertyUtil.getSchemaProperty("attribute_pgPackingLevel");
		String ObjectId="";
		String strtypePGPhase = PropertyUtil.getSchemaProperty("type_pgPhase");
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
		boolean isContextPushed = false;
		try{
			ContextUtil.pushContext(context);
			isContextPushed = true;
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
			if(objectList != null && !objectList.isEmpty())
			{
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// int intLevel = 0;
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				String strIsRootNode = "";
				String strValue ="";
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
				// String strLevel= "";
				// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
				String strObjectType="";
				String strObjectID = "";
				String strAttributeValue;
				DomainObject dobjObject =null;
				for (int i = 0; i < objectList.size(); i++)
				{
					strIsRootNode = (String)((Map)objectList.get(i)).get("Root Node");
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
					// strLevel= (String)((Map)objectList.get(i)).get("level");
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
					strObjectType=(String)((Map)objectList.get(i)).get("type");
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
					// intLevel = Integer.parseInt(strLevel);
					// Commented by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
					strObjectID = (String)((Map)objectList.get(i)).get(DomainConstants.SELECT_ID);
					if(!"true".equalsIgnoreCase(strIsRootNode))
					{
						// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 Start
						// Modified if condition to remove the level check
						// if(strtypePGPhase.equalsIgnoreCase(strObjectType)&&intLevel==1)
						if(strtypePGPhase.equalsIgnoreCase(strObjectType))
						// Modified by P&G IPM Team for Defect #5698 for Release 2013x.3.1 End
						{
							dobjObject = new DomainObject(strObjectID);
							strAttributeValue = (String)dobjObject.getInfo(context,"attribute["+strAttributeName+"]");
							vc.addElement(strAttributeValue);
						}
						else{
							vc.addElement("");
						}
					} else {
						vc.add("");
					}
				}
		//Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Starts
			}
		} catch(Exception e){
				throw e;
		} finally {
				if(isContextPushed){
				ContextUtil.popContext(context);
			}
		}
		// Added for Defect fix 7084 by DSM 2015x.1(Sogeti) Ends
		return vc;
	}
	/**
	 * This API gives id of latest Released revision of a particular Object
	 *
	 * @param context
	 * @param objectId: Id of the input Object
	 * @return String : id of latest Released revision of a given Object
	 * @throws Exception
	 */
	public static String getLatestReleasedObjectId(Context context,String objectId) throws Exception
	{
		String prevObjState="";
		String prevObjId="";
		String preObjRev="";
		String strObjectId=objectId;
		StringList selectList = new StringList();
		selectList.addElement(DomainConstants.SELECT_CURRENT);
		selectList.addElement(DomainConstants.SELECT_ID);
		DomainObject currentRevDObj =  new DomainObject(objectId);	
		String strRevision=(String)currentRevDObj.getInfo(context, DomainConstants.SELECT_REVISION);
		String strState=(String)currentRevDObj.getInfo(context, DomainConstants.SELECT_CURRENT);
		MapList revisionsInfoMapLst = currentRevDObj.getRevisionsInfo(context, selectList, new StringList());
		if(revisionsInfoMapLst.size()>1){
			Iterator itr = revisionsInfoMapLst.iterator();
			Map prevRevisionMap;
			while(itr.hasNext())
			{
				prevRevisionMap = (Map)itr.next();
				prevObjState=(String)prevRevisionMap.get(DomainConstants.SELECT_CURRENT);
				prevObjId=(String)prevRevisionMap.get(DomainConstants.SELECT_ID);		
				preObjRev=(String)prevRevisionMap.get(DomainConstants.SELECT_REVISION);	
				//Code Merge Start for 2013x Upgrade
				if(!prevObjId.equals(objectId) && Integer.parseInt(preObjRev)>Integer.parseInt(strRevision))
					//Code Merge Ends for 2013x Upgrade
				{
					strObjectId=prevObjId;
				}
			}
		}
		if(strObjectId==objectId && !"Release".equalsIgnoreCase(strState) ){
			strObjectId=getLatestObsoleteObjectId(context,objectId);
		}
		return strObjectId;
	}
	/**
	 * This API gives id of latest Obsolete revision of a particular Object
	 *
	 * @param context
	 * @param objectId: Id of the input Object
	 * @return String : id of latest Obsolete revision of a given Object
	 * @throws Exception
	 */
	public static String getLatestObsoleteObjectId(Context context,String objectId) throws Exception
	{
		String prevObjState="";
		String prevObjId="";
		String preObjRev="";
		String strObjectId=objectId;
		StringList selectList = new StringList();
		selectList.addElement(DomainConstants.SELECT_CURRENT);
		selectList.addElement(DomainConstants.SELECT_ID);
		DomainObject currentRevDObj =  new DomainObject(objectId);	
		String strRevision=(String)currentRevDObj.getInfo(context, DomainConstants.SELECT_REVISION);
		String strState=(String)currentRevDObj.getInfo(context, DomainConstants.SELECT_CURRENT);
		MapList revisionsInfoMapLst = currentRevDObj.getRevisionsInfo(context, selectList, new StringList());
		if(revisionsInfoMapLst.size()>1){
			Iterator itr = revisionsInfoMapLst.iterator();
			Map prevRevisionMap;
			while(itr.hasNext())
			{
				prevRevisionMap = (Map)itr.next();
				prevObjState=(String)prevRevisionMap.get(DomainConstants.SELECT_CURRENT);
				prevObjId=(String)prevRevisionMap.get(DomainConstants.SELECT_ID);		
				preObjRev=(String)prevRevisionMap.get(DomainConstants.SELECT_REVISION);	
				if(!prevObjId.equals(objectId) && "Obsolete".equals(prevObjState))
				{
					strObjectId=prevObjId;
				}
			}
		}
		return strObjectId;
	}
	/**
	 * This API gives Security Category Data for Product  in CPNProductDataSearchList Table
	 *
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getSecurityCategoryData(Context context,String[] args) throws Exception
	{
		Vector vc= new Vector();
		Map paramMap=(Map)JPO.unpackArgs(args);
		MapList objectList = (MapList) paramMap.get("objectList");
		HashMap paramListMap = (HashMap)paramMap.get("paramList");
		String strAttributeTitle = PropertyUtil.getSchemaProperty("attribute_Title");
		StringList slListSelect = new StringList(1);
		slListSelect.addElement("attribute["+strAttributeTitle+"]");
		String objIdArray[] = new String[objectList.size()];
		if(objectList != null && !objectList.isEmpty())
		{
			String strIsRootNode = "";
			String strValue ="";
			String strObjectID ="";
			String SecurityGroupName = "";
			StringList busSelects = new StringList();
			busSelects.addElement(DomainObject.SELECT_ID);
			busSelects.addElement(DomainObject.SELECT_NAME);
			String strGroupName="";
			DomainObject newDO=null;
			MapList pgSecurityGroupMapList=new MapList();
			Iterator itr = null;
			Map partDtlMap =null;
			for (int i = 0; i < objectList.size(); i++)
			{
				strGroupName = "";
				strObjectID = (String)((HashMap)objectList.get(i)).get(DomainConstants.SELECT_ID);
				newDO=new DomainObject(strObjectID);
				//Modified by DSM-2015x.1(Sogeti) for Search Requirement on 19-Apr-2016 - Starts
				if(newDO.hasRelatedObjects(context, pgV3Constants.RELATIONSHIP_CLASSIFIEDITEM, false)){
					pgSecurityGroupMapList = newDO.getRelatedObjects(context,
							pgV3Constants.RELATIONSHIP_CLASSIFIEDITEM, pgV3Constants.TYPE_IPCONTROLCLASS, busSelects,
							null, true,false, (short) 1, null, null, 0);
				//Modified by DSM-2015x.1(Sogeti) for Search Requirement on 19-Apr-2016 - Ends
					itr = pgSecurityGroupMapList.iterator();
					while(itr.hasNext()) {
						partDtlMap = (Map) itr.next();
						SecurityGroupName = (String) partDtlMap.get(DomainObject.SELECT_NAME);
						if(strGroupName.length()>0){
							strGroupName=strGroupName+"|"+SecurityGroupName;
						}else
							strGroupName=SecurityGroupName;
					}
					vc.add(strGroupName);
				}
				else  {
					vc.add("");
				}
			}
		}
		return vc;
	}
	//P&G: Added below methods by PLM V2 for EBOM table update :End	
	//Merged by 2013x upgrade team end -
	// Added below Code and methods by P&G VPD Team for Defect #1609 Start
	private static final String SELECT_ATTRIBUTE_DEFAULT_PART_POLICY = "attribute[" + PropertyUtil.getSchemaProperty("attribute_DefaultPartPolicy") + "]";
	private boolean isValidData(String data) {
		return ((data == null || "null".equals(data)) ? 0 : data.trim().length()) > 0;
	}
	private String getStringValue(Map map, String key) {
		return (String) map.get(key);
	}
	private int length(Object[] array) {
		return array == null ? 0 : array.length;
	}
	private StringList createStringList(String[] selectable) {
		int length = length(selectable);
		StringList list = new StringList(length);
		for (int i = 0; i < length; i++)
			list.add(selectable[i]);
		return list;
	}
	/**
	 * if objectId is of Part Family then policy will be returned from partfamily attribute "Deafult Part Policy"
	 * else it will return "Development Part" as default policy.
	 *
	 * @param context ematrix context
	 * @param map contains request map
	 * @return String policy name
	 * @throws Exception if error occurs
	 */
	private String getDefaultPolicy(Context context, HashMap map) throws Exception {
		String defaultPolicy = FrameworkProperties.getProperty(context, "type_Part.defaultDevPolicy");
		String objectId = getStringValue(map, "objectId");
		if (isValidData(objectId)) {
			StringList objectSelect = createStringList(new String[] {DomainConstants.SELECT_TYPE, SELECT_ATTRIBUTE_DEFAULT_PART_POLICY});
			DomainObject domObj = DomainObject.newInstance(context, objectId);
			Map dataMap = domObj.getInfo(context, objectSelect);
			String strType = getStringValue(dataMap, DomainConstants.SELECT_TYPE);
			if (DomainConstants.TYPE_PART_FAMILY.equals(strType)) {
				String strPolicy = getStringValue(dataMap, SELECT_ATTRIBUTE_DEFAULT_PART_POLICY);
				if (isValidData(strPolicy)) {
					defaultPolicy = PropertyUtil.getSchemaProperty(context,strPolicy);
				}
			}
		}
		return defaultPolicy;
	}
	/**
	 * To display the policy list in part creation page Also considers the app
	 * specific policies
	 *
	 * @param context
	 * @param args
	 * @return HashMap
	 * @throws Exception
	 * @Since R211
	 */
	public HashMap getPolicy(Context context, String[] args) throws Exception {
		HashMap hmPolicyMap = new HashMap();
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap requestMap = (HashMap) programMap.get("requestMap");
		String createMode = (String) requestMap.get("createMode");
		String typeString = (String) requestMap.get("type");
		// Added by VPD for Defect #1609 Start
		String strShapePartType = PropertyUtil.getSchemaProperty(context,"type_pgPKGVPMPart");
		String strFlexPartType = PropertyUtil.getSchemaProperty(context,"type_pgPKGFlexPart");
		String strTransportUnitPartType = PropertyUtil.getSchemaProperty(context,"type_pgPKGTransportUnitPart");
		StringList slCustomTypes = new StringList(strShapePartType);
		slCustomTypes.addElement(strFlexPartType);
		slCustomTypes.addElement(strTransportUnitPartType);
		// Added by VPD for Defect #1609 End
		try {
			String POLICY_UNRESOLVED_PART = PropertyUtil.getSchemaProperty(context, "policy_UnresolvedPart"); // When upgrading this policy should be hidden in create part page.
			if(typeString.startsWith("type_")){
				typeString = PropertyUtil.getSchemaProperty(context, typeString);
			}else if(typeString.startsWith("_selectedType")){
				typeString = typeString.substring(typeString.indexOf(':')+1, typeString.indexOf(','));
			}else if("".equals(typeString) || typeString==null){
				typeString = DomainConstants.TYPE_PART;
			}
			BusinessType partBusinessType = new BusinessType(typeString, context.getVault());
			if(!mxType.isOfParentType(context, typeString, DomainConstants.TYPE_PART)){
				throw new FrameworkException();
			}
			// Added by VPD for Defect #1609 Start
			//If the type is Shape Part, Flex Part or Transport Unit Part, Show the custom Policy
			String defaultPolicy = null;		
			StringList display = new StringList();
			StringList actualVal = new StringList();
			String languageStr = context.getSession().getLanguage();	
			if (!slCustomTypes.contains(typeString)) {
				// Added by VPD for Defect #1609 End
				PolicyList allPartPolicyList = partBusinessType
				.getPoliciesForPerson(context, false);
				PolicyItr partPolicyItr = new PolicyItr(allPartPolicyList);
				boolean isMBOMInstalled = FrameworkUtil.isSuiteRegistered(context,
						"appVersionX-BOMManufacturing", false, null, null);
				boolean bcamInstall = FrameworkUtil.isSuiteRegistered(context,
						"appVersionX-BOMCostAnalytics", false, null, null);
				boolean isECCInstall = FrameworkUtil.isSuiteRegistered(context,
						"appVersionEngineeringConfigurationCentral", false, null,
						null);
				String POLICY_STANDARD_PART = PropertyUtil
				.getSchemaProperty(context,"policy_StandardPart");
				String POLICY_CONFIGURED_PART = PropertyUtil
				.getSchemaProperty(context,"policy_ConfiguredPart");
				defaultPolicy = getDefaultPolicy(context, requestMap); // IR-082946V6R2012
				Policy policyValue = null;
				String policyName = "";
				String policyClassification = "";
				if (createMode.equals("assignTopLevelPart") || POLICY_CONFIGURED_PART.equals(defaultPolicy)) {
					display.addElement(i18nNow.getAdminI18NString("Policy",
							POLICY_CONFIGURED_PART, languageStr));
					actualVal.addElement(POLICY_CONFIGURED_PART);
				} else if(createMode.equals("MFG")) {
					StringList slMfgPolicy = EngineeringUtil.getManuPartPolicy(context);
					if(slMfgPolicy.size()>0) {
						defaultPolicy = (String)slMfgPolicy.get(0);
					}
					for(int i=0; i<slMfgPolicy.size(); i++)
					{
						policyName = (String)slMfgPolicy.get(i);
						if(EngineeringUtil.getPolicyClassification(context, policyName).equals("Equivalent")) {
							continue;
						} else if(policyName.equals(PropertyUtil.getSchemaProperty(context,"policy_StandardPart"))) {
							continue;
						}
						display.addElement(i18nNow.getAdminI18NString("Policy", policyName, languageStr));
						actualVal.addElement(policyName);
						if(i == 0) {
							defaultPolicy = (String)slMfgPolicy.get(i);
						}
						if(policyName.equals(PropertyUtil.getSchemaProperty(context,"policy_ManufacturingPart"))) {
							defaultPolicy = policyName;
						}
					}
				} else {
					while (partPolicyItr.next()) {
						policyValue = (Policy) partPolicyItr.obj();
						policyName = policyValue.getName();
						//when upgrading from previous release, skip this policy
						if (policyName.equals(POLICY_UNRESOLVED_PART)) {
							continue;
						}
						policyClassification = EngineeringUtil
						.getPolicyClassification(context, policyName);
						// Modified for TBE Packaging & Scalability
						if (!EngineeringUtil.isENGInstalled(context, args)&&
								!EngineeringUtil.getPolicyClassification(context,policyName).equals("Development" ))
						{
							continue;
						}
						if (!isMBOMInstalled
								&& POLICY_STANDARD_PART
								.equalsIgnoreCase(policyName)) {
							continue;
						}
						if (POLICY_CONFIGURED_PART.equalsIgnoreCase(policyName)) {
							continue;
						}
						if (bcamInstall) {
							if ("Cost".equals(policyClassification)) {
								continue;
							}
						}
						if (EngineeringUtil.getPolicyClassification(context,
								policyName).equals("Equivalent")
								|| EngineeringUtil.getPolicyClassification(context,
										policyName).equals("Manufacturing")) {
							continue;
						}
						display.addElement(i18nNow.getAdminI18NString("Policy",
								policyName, languageStr));
						actualVal.addElement(policyName);
					}
				}
				int position = actualVal.indexOf(defaultPolicy);
				if (position > 0) {
					String positionDisplay = (String) display.get(position);
					String positionActual = (String) actualVal.get(position);
					display.setElementAt(display.get(0), position);
					actualVal.setElementAt(actualVal.get(0), position);
					display.setElementAt(positionDisplay, 0);
					actualVal.setElementAt(positionActual, 0);
				}
				// Added by VPD for Defect #1609 Start
				//If the type is Shape Part, Flex Part or Transport Unit Part, Show the custom Policy
			}
			else
			{
				if (typeString.equals(strShapePartType)) {
					defaultPolicy = PropertyUtil.getSchemaProperty(context,"policy_pgPKGWIPPart");
				}
				else if (typeString.equals(strFlexPartType)) {
					defaultPolicy = PropertyUtil.getSchemaProperty(context,"policy_pgPKGFlexPart");
				}
				else if (typeString.equals(strTransportUnitPartType)) {
					defaultPolicy = PropertyUtil.getSchemaProperty(context,"policy_pgPKGTransportUnitPart");
				}				
				display.addElement(i18nNow.getAdminI18NString("Policy", defaultPolicy, languageStr));
				actualVal.addElement(defaultPolicy);
			}
			// Added by VPD for Defect #1609 End
			hmPolicyMap.put("field_choices", actualVal);
			hmPolicyMap.put("field_display_choices", display);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return hmPolicyMap;
	}
	// Added below Code and methods by P&G VPD Team for Defect #1609 End
	/**
	 * Synchronizes the changes made on an EBOM relationship with any Substitutes the EBOM has.
	 *      When the Find Number or Reference Designator attributes are updated on an EBOM,
	 *      we need to update the same attributes on any of its Substitue objects.
	 *
	 * @param context the eMatrix <code>Context</code> object.
	 * @param args holds the following input arguments:
	 *        0 - the EBOM relationship id that is being updated
	 *        1 - the attribute name being updated
	 *        2 - the current value of the attribute
	 *        3 - the new value of the attribute
	 * @throws Exception if the operation fails.
	 * @since 10.0.0.0.
	 * @trigger RelationshipEBOMModifyAttributeAction.
	 * Modified by Sogeti on 10/24/2013- We don't want to execute this trigger on CSS-PLM integration
	 */
	public void syncSubstitutes(Context context, String[] args)
	throws Exception
	{
		DebugUtil.debug("RelationshipEBOMModifyAttributeAction:syncSubstitutes");
		//Modified by Sogeti on 10/24/2013- We don't want to execute this trigger on CSS-PLM integration - start
		String strFromIntegration = PropertyUtil.getGlobalRPEValue(context, "FromIntegration");
		String strContextUser = (String)context.getUser();
		if ("sys_css".equals(strContextUser) || "Yes".equalsIgnoreCase(strFromIntegration)){
			return;
		}
		//Modified by Sogeti on 10/24/2013- We don't want to execute this trigger on CSS-PLM integration - End
		// args[] parameters
		//
		String ebomRelId = args[0];
		String attrbName = args[1];
		String currentValue = args[2];
		String newValue = args[3];
		//Added for X3 Start
		StringList slEBOMSubstituteRel = new StringList();
		//        HashMap attmap = new HashMap();
		//String  RELATIONSHIP_EBOM_SUBSTITUTE        =
		//	PropertyUtil.getSchemaProperty(context,"relationship_EBOMSubstitute");
		
		//Added for X3 end
		DebugUtil.debug("ebomRelId = " + ebomRelId);
		DebugUtil.debug("attrName  = " + attrbName);
		DebugUtil.debug("currentValue = " + currentValue);
		DebugUtil.debug("newValue  = " + newValue);
		//added for the bug 329326
		if("*".equals(currentValue.trim())) {
			currentValue = "";
		}
		//till here
		//Modified by DSM(Sogeti)-2015x.4 for Revision Rule (Defect ID- 14472) on 28-09-2017 - Starts
		//if (attrbName.equals(ATTRIBUTE_FIND_NUMBER) || attrbName.equals(ATTRIBUTE_REFERENCE_DESIGNATOR) || attrbName.equals(ATTRIBUTE_QUANTITY))
		if (attrbName.equals(ATTRIBUTE_FIND_NUMBER) || attrbName.equals(ATTRIBUTE_REFERENCE_DESIGNATOR))
		//Modified by DSM(Sogeti)-2015x.4 for Revision Rule (Defect ID- 14472) on 28-09-2017 - Starts
		{
			try
			{
				//Added for X3 Start
				RelToRelUtil ebomReltoRel = new RelToRelUtil();
				//getting conneted EBOM Substitute connection id
				slEBOMSubstituteRel = ebomReltoRel.getFrommids(context, ebomRelId, RELATIONSHIP_EBOM_SUBSTITUTE);
				Iterator ebomsubsItr        = slEBOMSubstituteRel.iterator();
				String sEBOMSubstituteRelid = "";
				//Added for IR-021267
				RelToRelUtil domRel = null;
				while(ebomsubsItr.hasNext())
				{
					//EBOM Substitute Id
					sEBOMSubstituteRelid            = (String) ebomsubsItr.next();
					//Added for IR-021267
					domRel = new RelToRelUtil(sEBOMSubstituteRelid);
					//Synchronizes the changes made on an EBOM relationship with any Substitutes the EBOM has.
					domRel.setAttributeValue(context, attrbName, newValue);
				}//end While ebomsubsItr
				//Added for X3 End
				// removed commented code
			}
			catch (Exception e)
			{
				DebugUtil.debug("triggerRelationshipEBOMModifyAction-----Exception=", e.toString());
				throw (e);
			}
			finally
			{
			}
		}
	}
	/**
	 * This method is called on changing Attribute "Quantity" on Relationship EBOM/EBOM Substitute
	 * to disconnect Split Quantity connected with respected Part
	 * @param context the eMatrix <code>Context</code> object
	 * @param args holds the input arguments: RelID of EBOM/EBOM Substitute
                                                 AttributeName
                                                 Current Value
                                                 New Value
	 * @throws Exception if the operation fails
	 */
	public void updateTotal(Context context, String[] args)
	throws Exception
	{
		//Modified by Sogeti on 10/24/2013- We don't want to execute this trigger on CSS-PLM integration - start
		String strFromIntegration = PropertyUtil.getGlobalRPEValue(context, "FromIntegration");
		String strContextUser = (String)context.getUser();
		if ("sys_css".equals(strContextUser) || "Yes".equalsIgnoreCase(strFromIntegration)){
			return;
		}
		//Modified by Sogeti on 10/24/2013- We don't want to execute this trigger on CSS-PLM integration - End
		String ebomRelId = args[0];
		String attrbName = args[1];
		String currentValue = args[2];
		String newValue = args[3];
		String relpattern="";
		if (attrbName.equals(DomainConstants.ATTRIBUTE_QUANTITY) || attrbName.equals(PropertyUtil.getSchemaProperty(context,"attribute_Loss")))
		{
			DomainRelationship ebomRel = new DomainRelationship(args[0]);
			ebomRel.open(context);
			String strLossValue = "0";
			String strQtyValue = "0";
			if (attrbName.equals(DomainConstants.ATTRIBUTE_QUANTITY)) {
				strQtyValue = newValue;
				strLossValue = ebomRel.getAttributeValue(context, PropertyUtil.getSchemaProperty(context,"attribute_Loss"));
			} else {
				strQtyValue = ebomRel.getAttributeValue(context, DomainConstants.ATTRIBUTE_QUANTITY);
				strLossValue = newValue;
			}
			String strTotal = (new Float((new Float(strQtyValue)).intValue() - (new Float(strLossValue)).intValue())).toString();
			ebomRel.setAttributeValue(context,PropertyUtil.getSchemaProperty(context,"attribute_Total"), strTotal);
			ebomRel.close(context);
		}
	}//End of disconnectSplitQuantity
	/**
	 * DSO - Impact Assessment : This method is used to connect the ECO to AffectedFPPList Id List when is getting created/connected to Part in AffectedFPPList ID List view.
	 * @param context the eMatrix <code>Context</code> object
	 * @param args holds the input arguments
	 * @throws Exception if the operation fails
	 */
	public void postProcessForAddECO(Context context,String[]args) throws FrameworkException
	{
		try {
			StringList sObjList = null;
			String strSelPartId= "";
			Map paramMap=(Map)JPO.unpackArgs(args);	
			Map mpRequest = (Map) paramMap.get("requestMap");
			String strPartID = (String) mpRequest.get("objectId");
			String strObjIds = (String) mpRequest.get("strObjIds");
			//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - START
			String strChangeTemplateOID = (String) mpRequest.get("ChangeTemplateOID");
			String strResponsibleOrganizationOID =  (String) mpRequest.get("ResponsibleOrganizationOID");
			//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - END
			HashMap paramListMap = (HashMap)paramMap.get("paramMap");
			//DSM(DS) 2015x.1 New ECO can't created and added to FPP under AFPP Start
			String strNewECOId 			= (String)paramListMap.get("newObjectId");
			//DSM(DS) 2015x.1 New ECO can't created and added to FPP under AFPP End
			String rootObjectId = (String) mpRequest.get("rootObjectId");
			//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - START
			if(UIUtil.isNotNullAndNotEmpty(strResponsibleOrganizationOID) || UIUtil.isNotNullAndNotEmpty(strChangeTemplateOID))
			{
				//DSM(DS) 2015x.1 commented as below statement will create new CO Start
				//String strECOID = ECMUtil.getChangeTempDetailsAndCreateCO(context, strChangeTemplateOID, strResponsibleOrganizationOID);
				//DSM(DS) 2015x.1 commented as below statement will create new CO End
				//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - END
				sObjList = FrameworkUtil.split(strObjIds,",");
				if(null != sObjList && sObjList.size()>0){
					for(int i=0; i<sObjList.size(); i++){
						strSelPartId = (String)sObjList.get(i).toString().trim();
						//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - START
						//DSM(DS) 2015x.1 New ECO can't created and added to FPP under AFPP Start
						if(UIUtil.isNotNullAndNotEmpty(strSelPartId) && UIUtil.isNotNullAndNotEmpty(strNewECOId))
						{
							Map tempParamMap = new HashMap();							
							//DSM(DS) 2015x.1 New ECO can't created and added to FPP under AFPP Start
							if(UIUtil.isNotNullAndNotEmpty(strChangeTemplateOID))
							{
								tempParamMap.put(CPNCommonConstants.CHT_IDS,strChangeTemplateOID);
							}
							//DSM(DS) 2015x.1 New ECO can't created and added to FPP under AFPP End
							tempParamMap.put(CPNCommonConstants.CO_IDS,strNewECOId);

							tempParamMap.put("rdoId",strResponsibleOrganizationOID);
							
							tempParamMap.put("strProdDataId",strSelPartId);
							
							ECMUtil.createAndConnectCO(context, JPO.packArgs(tempParamMap));
						}
						//DSM(DS) 2015x.1 New ECO can't created and added to FPP under AFPP End
						//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - END
					} //end of if loop
				} //end of for loop
			//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - START
			}
			//DSM 2015x.1 - To connect the CO to AffectedFPPList Affected Items - END
		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkException(e);    		
		}
	}
	//DSM DS 2015x.4 ALM 14496 - Code modified for testing purpose on MDEV - for performance issue - Start
	public MapList getPartWhereUsedCustom(Context context, String[] args) throws Exception {		
		MapList finalListReturn = new MapList();
		boolean isContextPushed = false;
		try
		{		
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			String objectId = getStringValue(programMap, "objectId");
			//DSM (DS) 2015x.4.1 Feb ALM 16034 and 2015x.5 ALM 15588 - Object not selectable - START
			//String parentId = getStringValue(programMap, "parentId");		
			String parentId = getStringValue(programMap, "parentOID");
			//DSM (DS) 2015x.4.1 Feb ALM 16034 and 2015x.5 ALM 15588 - Object not selectable - END
			if (UIUtil.isNotNullAndNotEmpty(objectId)) 
			{				
				DomainObject domObj = DomainObject.newInstance(context, objectId);
				if (FrameworkUtil.hasAccess(context, domObj , "read")) 
				{
					StringList slWhereUsedRelationshipList = new StringList();
					slWhereUsedRelationshipList.addElement(DomainConstants.RELATIONSHIP_EBOM);
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.REL_STARTING_MATERIAL);				
					slWhereUsedRelationshipList.addElement(DomainConstants.RELATIONSHIP_ALTERNATE);
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.REL_PG_TRANSPORT_UNIT);
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.REL_PLANNED_FOR);
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.REL_FORMULATION_PROPAGATE);
					slWhereUsedRelationshipList.addElement(RELATIONSHIP_EBOM_SUBSTITUTE);
					//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.REL_FBOM);
					//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends
					//DSM (DS) 2018.0 21839 AFPP is not showing in Where used tab of FPP -starts
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.REL_PG_AffectedFPPLIST);
					//DSM (DS) 2018.0 21839 AFPP is not showing in Where used tab of FPP -Ends
					//DSM (DS) 2018x.0 ALM 21845 "Where Used" displaying wrong data on RMP - STARTS
					//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts
					//slWhereUsedRelationshipList.addElement(FormulationRelationship.FORMULA_INGREDIENT.getRelationship(context));
					//DSM(DS) 2018x.0 ALM 22076 ENDS - Where Used completion taking long time for highly connected parts
					//DSM (DS) 2018x.0 ALM 21845 "Where Used" displaying wrong data on RMP - ENDS
					//SJV4-Added trigger method for SmartScope development :: START
					slWhereUsedRelationshipList.addElement(${CLASS:pgDSOConstants}.RELATIONSHIP_PGPRODUCTPLATFORMMATERIAL);
					//SJV4-Added trigger method for SmartScope development :: END
					ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
					isContextPushed = true;					
					StringList slObjectSelect = new StringList();
					if(null != slWhereUsedRelationshipList && !slWhereUsedRelationshipList.isEmpty())
					{
						for(int i=0 ; i < slWhereUsedRelationshipList.size() ; i++)
						{
							slObjectSelect.addElement("to[" + slWhereUsedRelationshipList.get(i).toString() + "]");
						}
					}
					//StringList slObjectSelect = createStringList(new String[] {"to[EBOM]", "to[Starting Material]","to[EBOM Substitute]","to[Alternate]", "to[pgTransportUnit]", "to[PLBOM]", "to[Planned For]", "to[Formulation Propagate]"});
					Map mpAssociatedConnections = domObj.getInfo(context,slObjectSelect);
					if(null != mpAssociatedConnections && !mpAssociatedConnections.isEmpty())
					{
						//DSM(DS) 2018x.2 - Fix for ALM 28940 - Where Used category of an Raw Material Part is not showing entire Where Used list - START
						//if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+DomainConstants.RELATIONSHIP_EBOM+"]")))
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+DomainConstants.RELATIONSHIP_EBOM+"]")) || 
							"true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+FormulationRelationship.FORMULA_INGREDIENT.getRelationship(context)+"]")))
						//DSM(DS) 2018x.2 - Fix for ALM 28940 - Where Used category of an Raw Material Part is not showing entire Where Used list - END
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,DomainConstants.RELATIONSHIP_EBOM));
						}
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.REL_STARTING_MATERIAL+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.REL_STARTING_MATERIAL));
						}
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+DomainConstants.RELATIONSHIP_ALTERNATE+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,DomainConstants.RELATIONSHIP_ALTERNATE));
						}
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.REL_PG_TRANSPORT_UNIT+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.REL_PG_TRANSPORT_UNIT));
						}
						//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.REL_FBOM+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.REL_FBOM));
						}
						//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.REL_PLANNED_FOR+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.REL_PLANNED_FOR));
						}
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.REL_FORMULATION_PROPAGATE+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.REL_FORMULATION_PROPAGATE));
						}
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+RELATIONSHIP_EBOM_SUBSTITUTE+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.RELATIONSHIP_EBOM_SUBSTITUTE));
						}
						//DSM (DS) 2018.0 21839 AFPP is not showing in Where used tab of FPP -starts
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.REL_PG_AffectedFPPLIST+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.REL_PG_AffectedFPPLIST));
						}
						//DSM (DS) 2018.0 21839 AFPP is not showing in Where used tab of FPP -Ends
						//DSM (DS) 2018x.0 ALM 21845 "Where Used" displaying wrong data on RMP - STARTS
						//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts
						/*if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+FormulationRelationship.FORMULA_INGREDIENT.getRelationship(context)+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,FormulationRelationship.FORMULA_INGREDIENT.getRelationship(context)));
						}*/
						//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts
						//DSM (DS) 2018x.0 ALM 21845 "Where Used" displaying wrong data on RMP - ENDS
						//SJV4-Added trigger method for SmartScope development :: START
						if("true".equalsIgnoreCase((String)mpAssociatedConnections.get("to["+${CLASS:pgDSOConstants}.RELATIONSHIP_PGPRODUCTPLATFORMMATERIAL+"]")))
						{
							finalListReturn.addAll(getWhereUsedList(context,objectId,parentId,${CLASS:pgDSOConstants}.RELATIONSHIP_PGPRODUCTPLATFORMMATERIAL));
						}
						//SJV4-Added trigger method for SmartScope development :: END
					}		
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		}
		finally
		{
			if(isContextPushed)
			{
				ContextUtil.popContext(context);
			}
		}
		return finalListReturn;
	}
	private MapList getWhereUsedList (Context context, String strObjectId, String strContextObjectId, String strRelationshipName) throws Exception
	{
		MapList mlReturnList = new MapList();
		String strID = new String();
		String strType = new String();
		String strObjectWhere = DomainConstants.SELECT_TYPE + "!=" + pgV3Constants.TYPE_PGPHASE;
		String strAssociatedParents = new String();
		StringList slAssociatedParent = new StringList();
		String strSubstituteParents = new String();
		StringList slSubstituteParents = new StringList();
		StringList slObjectSelect = new StringList(DomainConstants.SELECT_ID);
		slObjectSelect.addElement(DomainConstants.SELECT_NAME);
		Map mpTemp = new HashMap();
		//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts (additional parameter in query)
		//String strMqlCommand1 = "print bus $1 select $2 $3 dump $4";
		String strMqlCommand1 = "print bus $1 select $2 $3 $4 dump $5";
		String strRelName = new String();
		//DSM(DS) 2018x.0 ALM 22076 ENDS - Where Used completion taking long time for highly connected parts
		String strMqlCommand2 = "print bus $1 select $2 dump $3";
		//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
		if(${CLASS:pgDSOConstants}.REL_FBOM.equals(strRelationshipName) || DomainConstants.RELATIONSHIP_EBOM.equals(strRelationshipName))
		{
			//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends
			//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts (retrieve rel name)
			//strAssociatedParents  = MqlUtil.mqlCommand(context, strMqlCommand1, strObjectId,"to[" + strRelationshipName + "].from.id", "to[" + strRelationshipName + "].from.type", "|");
			strAssociatedParents  = MqlUtil.mqlCommand(context, strMqlCommand1, strObjectId,"to[" + strRelationshipName + "].from.id", "to[" + strRelationshipName + "].from.type","to["+ strRelationshipName + "].name", "|");
			//DSM(DS) 2018x.0 ALM 22076 ENDS - Where Used completion taking long time for highly connected parts
			if(UIUtil.isNotNullAndNotEmpty(strAssociatedParents))
			{
				slAssociatedParent.clear();
				slAssociatedParent = FrameworkUtil.split(strAssociatedParents, "|");				
				if(null != slAssociatedParent && !slAssociatedParent.isEmpty())
				{
					//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts (extra param in results)
					//int x = slAssociatedParent.size()/2;
					int x = slAssociatedParent.size()/3;
					//DSM(DS) 2018x.0 ALM 22076 ENDS - Where Used completion taking long time for highly connected parts
					for(int i=0 ; i < x ; i++)
					{						
						strID = slAssociatedParent.get(i).toString();
						strType = slAssociatedParent.get(i+x).toString();
						//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts
						strRelName = slAssociatedParent.get(i+x+x).toString();
						//DSM(DS) 2018x.0 ALM 22076 ENDS - Where Used completion taking long time for highly connected parts
						//DSM(DS) 2018x.2 - Fix for ALM 28940 - Where Used category of an Raw Material Part is not showing entire Where Used list - START
						//if(DomainConstants.RELATIONSHIP_EBOM.equals(strRelationshipName) && UIUtil.isNotNullAndNotEmpty(strType) && pgV3Constants.TYPE_PGPHASE.equals(strType))
						if(DomainConstants.RELATIONSHIP_EBOM.equals(strRelName) && UIUtil.isNotNullAndNotEmpty(strType) && pgV3Constants.TYPE_PGPHASE.equals(strType))
						//DSM(DS) 2018x.2 - Fix for ALM 28940 - Where Used category of an Raw Material Part is not showing entire Where Used list - END
						{
							mpTemp = new HashMap();
							/*
							DomainObject doPhase = DomainObject.newInstance(context,strID);
							MapList mlList = doPhase.getRelatedObjects(context,
															strRelationshipName,
															${CLASS:pgDSOConstants}.TYPE_PART,
															slObjectSelect,
															null,
															true,
															false,
															(short)1,
															strObjectWhere,
															null,
															(short)0);
							if(null != mlList && !mlList.isEmpty())
							{
								Map mp = (Map)mlList.get(0);
								mpTemp.put("PhaseParentID", (String)mp.get(DomainConstants.SELECT_ID));
								mpTemp.put("PhaseParentName", (String)mp.get(DomainConstants.SELECT_NAME));
							}
							*/
							mpTemp.put(DomainConstants.SELECT_ID, strID);
							mpTemp.put("relationship", strRelationshipName);
							mpTemp.put("disableSelection", "true");
							mpTemp.put("RowEditable","false");
							mlReturnList.add(mpTemp);
						}
						//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
						else if(${CLASS:pgDSOConstants}.REL_FBOM.equals(strRelationshipName) && UIUtil.isNotNullAndNotEmpty(strType) && ${CLASS:pgDSOConstants}.TYPE_PARENT_SUB.equals(strType))
						{
							//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends
							//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
							strSubstituteParents  = MqlUtil.mqlCommand(context, strMqlCommand2, strID,"to[" + ${CLASS:pgDSOConstants}.RELATIONSHIP_FBOM_SUBSTITUTE + "].fromrel.from.id", "|");
							//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends
							if(UIUtil.isNotNullAndNotEmpty(strSubstituteParents))
							{
								slSubstituteParents.clear();
								slSubstituteParents = FrameworkUtil.split(strSubstituteParents, "|");
								if(null != slSubstituteParents && !slSubstituteParents.isEmpty())
								{
									for(int y = 0 ; y <slSubstituteParents.size() ; y++)
									{
										mpTemp = new HashMap();
										mpTemp.put(DomainConstants.SELECT_ID, slSubstituteParents.get(y).toString());
										//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
										mpTemp.put("relationship", ${CLASS:pgDSOConstants}.RELATIONSHIP_FBOM_SUBSTITUTE);
										//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends
										mpTemp.put("disableSelection", "true");
										mpTemp.put("RowEditable","false");
										mlReturnList.add(mpTemp);
									}
								}
							}
						}
						else
						{
							mpTemp = new HashMap();
							mpTemp.put(DomainConstants.SELECT_ID, strID);
							//DSM(DS) 2018x.0 ALM 22076 STARTS - Where Used completion taking long time for highly connected parts
							//mpTemp.put("relationship", strRelationshipName);
							mpTemp.put("relationship", strRelName);
							//DSM(DS) 2018x.0 ALM 22076 ENDS - Where Used completion taking long time for highly connected parts
							if(!(strObjectId.equalsIgnoreCase(strContextObjectId) && strRelationshipName.equalsIgnoreCase(DomainConstants.RELATIONSHIP_EBOM)))
							{					
								mpTemp.put("disableSelection", "true");
							}
							mpTemp.put("RowEditable","false");
							mlReturnList.add(mpTemp);
						}
						//mpTemp.put("RowEditable","false");
					}
				}
			}
		}
		else 
		{	
			String strSelect = new String();
			if(RELATIONSHIP_EBOM_SUBSTITUTE.equals(strRelationshipName))
			{
				strSelect = "to[" + strRelationshipName + "].fromrel.from.id";
			}
			else
			{
				strSelect = "to[" + strRelationshipName + "].from.id";
			}
			strAssociatedParents  = MqlUtil.mqlCommand(context, strMqlCommand2, strObjectId,strSelect, "|");
			if(UIUtil.isNotNullAndNotEmpty(strAssociatedParents))
			{
				slAssociatedParent.clear();
				slAssociatedParent = FrameworkUtil.split(strAssociatedParents, "|");
				if(null != slAssociatedParent && !slAssociatedParent.isEmpty())
				{
					for(int y = 0 ; y <slAssociatedParent.size() ; y++)
					{
						mpTemp = new HashMap();
						mpTemp.put(DomainConstants.SELECT_ID, slAssociatedParent.get(y).toString());
						mpTemp.put("relationship", strRelationshipName);
						mpTemp.put("disableSelection", "true");
						mpTemp.put("RowEditable","false");
						mlReturnList.add(mpTemp);
					}
				}	
			}
		}
		return mlReturnList;		
	}
	//DSM DS 2015x.4 ALM 14496 - Code modified for testing purpose on MDEV - for performance issue - End

	 /**
	 * DSO - Impact Assessment: This method is used to connect the ECR to AffectedFPPList Id List when is getting created/connected to Part in AffectedFPPList ID List view.
	 * @param context the eMatrix <code>Context</code> object
	 * @param args holds the input arguments
	 * @throws Exception if the operation fails
	 */
public void postProcessForAddECR(Context context,String[]args) throws FrameworkException
	{
		try {
			StringList sObjList = null;
			String strSelPartId= "";
			Map paramMap=(Map)JPO.unpackArgs(args);	
			Map mpRequest = (Map) paramMap.get("requestMap");
			String strPartID = (String) mpRequest.get("objectId");
			String strObjIds = (String) mpRequest.get("strObjIds");
			HashMap paramListMap = (HashMap)paramMap.get("paramMap");
			String strECRID = (String) paramListMap.get("newObjectId");
			String rootObjectId = (String) mpRequest.get("rootObjectId");
			sObjList = FrameworkUtil.split(strObjIds,",");
			if(null != sObjList && sObjList.size()>0){
				for(int i=0; i<sObjList.size(); i++){
					strSelPartId = (String)sObjList.get(i).toString().trim();
					if(UIUtil.isNotNullAndNotEmpty(strSelPartId) && UIUtil.isNotNullAndNotEmpty(strECRID) && !(strSelPartId.equalsIgnoreCase(strPartID)))
						MqlUtil.mqlCommand(context, "connect bus "+strECRID+" relationship \'"+DomainConstants.RELATIONSHIP_AFFECTED_ITEM+"\' to "+strSelPartId);
					String affectedItemRelId = getRelId(context, strECRID, strSelPartId, DomainConstants.RELATIONSHIP_AFFECTED_ITEM,TYPE_PRODUCT_DATA);
					connectAffectedFPPObjectToAffectedItem(context, rootObjectId, affectedItemRelId);
				} //end of if loop
			}	 //end of for loop
		} catch (Exception e) {
			e.printStackTrace();
			throw new FrameworkException(e);    		
		}
	}
	/**
	 * DSO - Impact Assessment: This method is used to connect the Affected Item relation to AffectedFPPList Id List when is getting created/connected to Part in AffectedFPPList ID List view.
	 * @param context the eMatrix <code>Context</code> object
	 * @param rootObjectId holds the context ObjectId which can be Project Space OR AffectedFPPList ID List id
	 * @param affectedItemRelId holds the relationship id of Affected Item relationship between ECO/ECR and Part
	 * @throws Exception if the operation fails
	 */
	private void connectAffectedFPPObjectToAffectedItem(Context context, String rootObjectId, String affectedItemRelId) throws Exception{
		try{
			String fromObjectId = "";
			if (UIUtil.isNotNullAndNotEmpty(rootObjectId)) {
				DomainObject rootObject = DomainObject.newInstance(context, rootObjectId);
				String type = rootObject.getInfo(context, SELECT_TYPE);
				if (${CLASS:pgDSOConstants}.TYPE_PG_AffectedFPPLIST.equals(type)) {
					fromObjectId = rootObjectId;
				} else if (CPNCommonConstants.TYPE_PROJECT_SPACE.equals(type)){
					String typeAffectedFPPList = ${CLASS:pgDSOConstants}.TYPE_PG_AffectedFPPLIST;
					StringList objSelects = UTILS.createSelects(DomainConstants.SELECT_ID);
					MapList AffectedFPPListInfo = rootObject.getRelatedObjects(context,
							DomainRelationship.RELATIONSHIP_PROJECT_VAULTS,  typeAffectedFPPList, objSelects, null,
							false, true, (short)1, null, null, 0);
					if(AffectedFPPListInfo != null && AffectedFPPListInfo.size()>0){
						String AffectedFPPListId = (String)((Map)(AffectedFPPListInfo.get(0))).get(DomainConstants.SELECT_ID);
						if(UIUtil.isNotNullAndNotEmpty(AffectedFPPListId)){
							fromObjectId = AffectedFPPListId;
						}
					}
				}
			}
			if (UIUtil.isNotNullAndNotEmpty(fromObjectId) && UIUtil.isNotNullAndNotEmpty(affectedItemRelId)) {
				StringBuffer mqlCommand = new StringBuffer();
				mqlCommand.append("add connection '").append(${CLASS:pgDSOConstants}.REL_PG_AffectedFPPLISTTOCHANGE).append("' from ").append(fromObjectId).append(" torel ").append(affectedItemRelId);
				MqlUtil.mqlCommand(context, mqlCommand.toString());
			}
		}catch(FrameworkException fwe){
			fwe.printStackTrace();
			throw new FrameworkException(fwe);
		}
	}
	/**
	 * DSO - Impact Assessment: This method is used to get relationship Id between the from and to object.
	 * @param context the eMatrix <code>Context</code> object
	 * @param fromObjId holds the from ObjectId
	 * @param toObjId holds the from ObjectId
	 * @param relationshipName holds the relationship name
	 * @param typePattern holds the type pattern
	 * @throws Exception if the operation fails
	 */
	private String getRelId(Context context, String fromObjId, String toObjId, String relationshipName,String typePattern) throws Exception {
		String relId = "";
		if (UIUtil.isNotNullAndNotEmpty(fromObjId)) {
			DomainObject fromObject = DomainObject.newInstance(context, fromObjId);
			StringList relSelects = UTILS.createSelects(DomainConstants.SELECT_RELATIONSHIP_ID);
			StringList objSelects = UTILS.createSelects(DomainConstants.SELECT_ID);
			MapList relatedObjects = fromObject.getRelatedObjects(context,
					relationshipName,  typePattern, objSelects, relSelects,
					false, true, (short)1, DomainConstants.EMPTY_STRING, null, 0);
			if(relatedObjects != null && relatedObjects.size()>0){
				for(Object objRelatedObject : relatedObjects)
				{	
					Map mpRelatedObject = (Map)objRelatedObject;
					if(UIUtil.isNotNullAndNotEmpty(toObjId)&& toObjId.equals(mpRelatedObject.get(DomainConstants.SELECT_ID).toString()))
					{
						relId = (String)mpRelatedObject.get(DomainConstants.SELECT_RELATIONSHIP_ID);
						break;
					}
				}
			}
		}
		return relId;
	}
	
	//DSO - Code changes for defect id -4092 - START
	/**
	 * This method checks for the connected document via "Reference Document" relationship.
	 * The types for validation would be considered based on the Key "emxCPN.TechnicalSpecification.TriggerValidation.TypeIncludeList"
	 * context
	 * args
	 * returns int
	 * @throws Exception
	 */
	public int checkConnectedReferenceDocument(Context context, String[] args) throws Exception{
		int iReturn = 0;
		try{
			String strQualitySpecificationId = args[0];
			String strPropertyKey = "emxCPN.TechnicalSpecification.TriggerValidation.TypeIncludeList";
			
			String strContextType = DomainConstants.EMPTY_STRING;
			String strReferenceDocumentId = DomainConstants.EMPTY_STRING;
			String strContextObjectName = DomainConstants.EMPTY_STRING;
			
			StringList slSelectable = new StringList(3);
			slSelectable.addElement(DomainConstants.SELECT_TYPE);
			slSelectable.addElement(DomainConstants.SELECT_NAME);
			slSelectable.addElement("from["+EngineeringConstants.RELATIONSHIP_REFERENCE_DOCUMENT+"].to."+DomainConstants.SELECT_ID);
			
			Map<String, String> mpSelectable = new HashMap();
			
			if(UIUtil.isNotNullAndNotEmpty(strQualitySpecificationId) ){
				DomainObject doQualitySpecificationObject = DomainObject.newInstance(context, strQualitySpecificationId);
				mpSelectable = doQualitySpecificationObject.getInfo(context, slSelectable);
				if(mpSelectable != null && mpSelectable.size()>0)
				{
					strContextType = mpSelectable.get(DomainConstants.SELECT_TYPE);
	
					String strSymbolicName = FrameworkUtil.getAliasForAdmin(context, "type", strContextType, true);
	
					boolean isTypeIncluded = isTypeIncluded(context, strPropertyKey, strSymbolicName);
	
					if(isTypeIncluded){
					
						strReferenceDocumentId = mpSelectable.get("from["+EngineeringConstants.RELATIONSHIP_REFERENCE_DOCUMENT+"].to."+DomainConstants.SELECT_ID);
						
						if(UIUtil.isNullOrEmpty(strReferenceDocumentId)){
	
							iReturn = 1;
	
							//DSO 2013x.4 - Added changes to throw custom alert for trigger failure : START
	
							strContextObjectName = mpSelectable.get(DomainConstants.SELECT_NAME);
	
							String strAlertMsg = i18nNow.getI18nString("emxCPN.CheckConnectedRefDoc.Alert", "emxCPNStringResource", context.getSession().getLanguage());
	
							strAlertMsg = strAlertMsg.concat(strContextObjectName);
	
							Exception ex = new Exception(strAlertMsg);
	
							throw ex;
	
							//DSO 2013x.4 - Added changes to throw custom alert for trigger failure : END							
	
						}
	
					}
				}
			}
		}catch(Exception fwe){
			//DSO 2013x.4 - Added changes to throw custom alert for trigger failure : START
			throw fwe;
			//DSO 2013x.4 - Added changes to throw custom alert for trigger failure : END
		}
		return iReturn;
	}
	/**
	 * Private method for checking a type against a property key which has a value as list of symbolic names seperated by ",".
	 * @param context
	 * @param strPropertyKey
	 * @param strContextType
	 * @return
	 */
	private boolean isTypeIncluded(Context context, String strPropertyKey, String strContextType){
		boolean isTypeIncluded = false;
		try{
			String strPropertyValue = FrameworkProperties.getProperty(context, strPropertyKey);
			StringList slPropertyList = FrameworkUtil.split(strPropertyValue, ",");
			if(slPropertyList != null && !slPropertyList.isEmpty()){
				isTypeIncluded = slPropertyList.contains(strContextType);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return isTypeIncluded;
	}
	//DSO - Code changes for defect id -4092 - END	
	  /**
     * Method to associate an interface to the  part
     * and set the "Part Family Reference" attribute as "Master or Reference or Unassigned".
     *
     * @mx.whereUsed Invoked by the trigger object on creation of Part-
     *               TypePartCreateAction,
     * @mx.summary This method associates External Part Data
     *             interface to a Equivalent part. Uses
     *             <code>MqlUtil.mqlCommand</code> to associate the interface
     *             to the part. Uses <code>DomainObject.setAttribute() </code>to
     *             set an attribute value
     * @param context
     *            the eMatrix <code>Context</code> object
     * @throws FrameworkException
     *             if the operation fails
     * @since EC X3
     */
	public int addInterfaceToPart(Context context, String args[])throws Exception {
		//DSO Change - Pushing Context to Super User - Start
		boolean isContextPushed = false;
		//DSO Change - Pushing Context to Super User - End
		int status = 0;
		String ObjectId = args[0]; // ${OBJECTID}
		String sFromObjectId = args[1]; // ${FROMOBJECTID}
		try {
			if(sFromObjectId==null || ObjectId==null || sFromObjectId.equals("") || ObjectId.equals("")) {
				return 1;
			}
			DomainObject doFromObject = DomainObject.newInstance(context,ObjectId);
			DomainObject doToObject = DomainObject.newInstance(context,sFromObjectId);
			String sFromObjectType = doFromObject.getInfo(context, DomainConstants.SELECT_TYPE);
			// Get Created Part ObjectId from Environment Variables
			String partFamilyReference =  PropertyUtil.getSchemaProperty(context,"interface_PartFamilyReference");
			/* boolean isMEP = false;
			boolean PartseriesActive = checkPartSeriesEnabled ( context, args).booleanValue();
			if(PartseriesActive) {
				isMEP = true;
			}
			if(isMEP)
			{ */
				//372257 : Modified the check for all the subtype of Part instead of checking only type Part
				if(sFromObjectType.equals(DomainConstants.TYPE_PART_FAMILY) && doToObject.isKindOf(context, DomainConstants.TYPE_PART)) {
				//DSM DS 2018x.1 ALM 25611 Newly created MCUP part cannot be added to part family - STARTS
                        //if(!(MqlUtil.mqlCommand(context,"print bus \"$1\" select $2 dump;",sFromObjectId,"interface["+partFamilyReference+"]")).equals("TRUE")){
						//DSO Change - Pushing Context to Super User - Start
						ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
						isContextPushed = true;
						//DSO Change - Pushing Context to Super User - End
						if(!(MqlUtil.mqlCommand(context,"print bus \"$1\" select $2 dump;",sFromObjectId,"interface["+partFamilyReference+"]")).equals("TRUE")){
						//DSM DS 2018x.1 ALM 25611 Newly created MCUP part cannot be added to part family - ENDS
						MqlUtil.mqlCommand(context, "modify bus \"$1\" add interface \"$2\";",sFromObjectId,partFamilyReference);
					}
				}
			// }
		} catch (Exception e) {
			status = 1;
			throw new FrameworkException(e);
		} finally {
			//DSO Change - Pop Context to Super User - Start
			if(isContextPushed)
			{
				ContextUtil.popContext(context);
			}
			//DSO Change - Pop Context to Super User - Start
			return status;
		}
	}
   // DSO - Added to check the Stage rules on BOM view -  START
	/**
	 * This method is use as check trigger on EBOM relation ship.
	 * This method is use to check the rule
	 * 1. If a Parent is at the Production Stage all children must be at the Production stage.
         * 2. If the Parent is at the Pilot stage all children must be in Pilot or Production stage.
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public int checkStageOfChildPart(Context context, String[] args) throws Exception{
		int iReturn = 0;
		try{
			String strAllowedStages = "";
			if(args != null && args.length > 0){
				String strChildId = args[0];
				String strParentId = args[1];
				
				String strChildStageValue = DomainConstants.EMPTY_STRING;
				String strChildType = DomainConstants.EMPTY_STRING;
	
				StringList slChildSelectable =  new StringList();
				//Modified for 2018x Upgrade - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
				slChildSelectable.addElement(${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
				slChildSelectable.addElement(DomainConstants.SELECT_TYPE);
				Map<String, String> mpChildMap = new HashMap();				
				if(UIUtil.isNotNullAndNotEmpty(strChildId) && UIUtil.isNotNullAndNotEmpty(strParentId))
				{
					DomainObject domParent = DomainObject.newInstance(context,strParentId);
	
					DomainObject domChild  = DomainObject.newInstance(context,strChildId);
					//Modified for 2018x Upgrade - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
					String strParentStageValue = domParent.getInfo(context,${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
					
					//DSM 2015X.1 - PUSH CONTEXT IS REQUIRED -HIR- Context user not have read access- START
					ContextUtil.pushContext(context);
					mpChildMap = domChild.getInfo(context, slChildSelectable);
					ContextUtil.popContext(context);
					//DSM 2015X.1 - PUSH CONTEXT IS REQUIRED -HIR- Context user not have read access- END
				
					//JPO Compilation Fixing 2015XUPGRADE START
					//Modified for 2018x Upgrade - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
					strChildStageValue = mpChildMap.get(${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
					//JPO Compilation Fixing 2015XUPGRADE END
					strChildType = mpChildMap.get(DomainConstants.SELECT_TYPE);
					
					/* DSO 2013x.4 -  Stages for Relationship Matrix - Start*/
					
					String errorMessage= "";
					
					if(${CLASS:pgDSOConstants}.TYPE_PG_TRANSPORTUNIT.equals(strChildType))
					{
						errorMessage= i18nNow.getI18nString("emxCPN.CreatepgTransportUnit.InvalidStage.Error.Alert","emxCPNStringResource",context.getSession().getLanguage());
					}
					else
					{
						errorMessage= i18nNow.getI18nString("emxCPN.CreatePDBOM.InvalidStage.Error.Alert","emxCPNStringResource",context.getSession().getLanguage());
					}
					
					/* DSO 2013x.4 -  Stages for Relationship Matrix - End*/
	
					if(UIUtil.isNotNullAndNotEmpty(strParentStageValue) && UIUtil.isNotNullAndNotEmpty(strChildStageValue)){
						
					
					strAllowedStages = FrameworkProperties.getProperty(context,"emxCPN.CreatePDBOM."+strParentStageValue+".AllwedStages");
					
					//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP -starts
					//Allowed states will change when its a Formulation Part because of Phase incompatibility. 
					if(${CLASS:pgDSOConstants}.TYPE_FORMULATION_PART.equals(strChildType))
					{
						strAllowedStages = FrameworkProperties.getProperty(context,"emxCPN.CreatePDBOM."+strParentStageValue+".FormulationPart.AllwedStages");
					}
					
					//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP -ends
	
						if(!strAllowedStages.contains(strChildStageValue)){
	
							iReturn = 1;						
	
							${CLASS:emxContextUtil}.mqlNotice(context, errorMessage + strAllowedStages);
	
						}					
	
					}				
				}
			}
		}catch(FrameworkException fwe){
			fwe.printStackTrace();
		}
		return iReturn;
	}
	// DSO - Added to check the Stage rules on BOM view -  END
	/* DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - START*/
		/* Overridden from emxECPartBase  and modified for Where Used Enhancements*/
		/** This methods gets all the parent parts connected to it depending upon selected options.
	 * @param context ematrix context
	 * @param args contains arguments
	 * @return MapList
	 * @throws Exception if any exception occurs.
	 */
	public MapList getPartWhereUsed(Context context, String[] args) throws Exception {
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - start
		MapList mlCustomList = new MapList();
		boolean isContextPushed = false;
		//DSM (DS) 2015x.5 - ALM 16822 - Phase row should not be checked - START
		String strType = EMPTY_STRING;
		//DSM (DS) 2015x.5 - ALM 16822 - Phase row should not be checked - END
		try
		{
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - End
		
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		String objectId = getStringValue(programMap, "objectId");
		MapList finalListReturn = new MapList();
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - start		
		String strParentOID = getStringValue(programMap, "parentOID");
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - End
		
		/*DSM2015x - Added changes for DSM15X2-10 : Disable selection of next level parts on Where Used table - Start*/
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - commented following code - start
		/*
		${CLASS:pgDSOCommonUtils} UTILS = ${CLASS:pgDSOCommonUtils}.INSTANCE;
		String strParentOID = UTILS.getParam(args, "parentOID").toString();
		StringList slParentId = new StringList();
		if(UIUtil.isNotNullAndNotEmpty(strParentOID))
		{
			slParentId = DomainObject.newInstance(context, strParentOID).getInfoList(context, "to["+DomainConstants.RELATIONSHIP_EBOM+"].from."+DomainConstants.SELECT_ID);
		}
		*/
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - End
		
		if(isValidData(objectId)){
			/*DSM2015x - Added changes for DSM15X2-10 : Disable selection of next level parts on Where Used table - End*/
			//DSM 2015x.1 - External Security Changes for displaying 1 level whereused Parts - START
			//DSM(DS) 2018x.0 ALM 20589 STARTS - Where used filter refinements fails if context user does not have access to parent parts
			MapList accessList = DomainObject.getInfo( context, new String[] {objectId}, new StringList("current.access") );
			String accessMask = DomainConstants.EMPTY_STRING;
			if(accessList != null && !accessList.isEmpty()) {
				Map tempAccessMap = (Map)accessList.get(0);
				accessMask = (String)tempAccessMap.get("current.access");
			}
			//2018x.0 APOLLO ALM 25938 - Where Used Issue - start
			//if(UIUtil.isNotNullAndNotEmpty(accessMask) && accessMask.contains("read") && accessMask.contains("show")) {
			if(UIUtil.isNotNullAndNotEmpty(accessMask) && ((accessMask.contains("read") && accessMask.contains("show")) || accessMask.contains("all"))) {				
			//2018x.0 APOLLO ALM 25938 - Where Used Issue - end				
			//DomainObject domObj = DomainObject.newInstance(context, objectId);
			//if (FrameworkUtil.hasAccess(context, domObj , "read")) {
			DomainObject domObj = DomainObject.newInstance(context, objectId);
			//DSM(DS) 2018x.0 ALM 20589 ENDS
			//DSM 2015x.1 - External Security Changes for displaying 1 level whereused Parts - END
				MapList endNodeList = null;
				MapList ebomSubstituteList = null;
				MapList spareSubAltPartList = null;
				MapList partWhereUsedEBOMList = null;
				MapList tupWhereUsedList = null;
				String strSelectedFN = getStringValue(programMap, "ENCPartWhereUsedFNTextBox");
				String strEffectivity = getStringValue(programMap, "CFFExpressionFilterInput");
				String strSelectedLevel = getStringValue(programMap, "ENCPartWhereUsedLevel");
				String strEffectivityOID = getStringValue(programMap, "CFFExpressionFilterInput_actualValue");
				String strSelectedRefDes = getStringValue(programMap, "ENCPartWhereUsedRefDesTextBox");
				String strEBOMSubstitute = getStringValue(programMap, "displayEBOMSub");
				String strSelectedRelated = getStringValue(programMap, "ENCPartWhereUsedRelated");
				String strSelectedRevisions = getStringValue(programMap, "ENCPartWhereUsedRevisions");
				String strSelectedLevelValue = getStringValue(programMap, "ENCPartWhereUsedLevelTextBox");
				String strSelectedState = getStringValue(programMap, "pgPartWhereUsedState");
				String REV_ALL = UINavigatorUtil.getI18nString("emxEngineeringCentral.Part.WhereUsedRevisionAll", "emxEngineeringCentralStringResource", "en");
				String LEVEL_ALL = UINavigatorUtil.getI18nString("emxEngineeringCentral.Part.WhereUsedLevelAll", "emxEngineeringCentralStringResource", "en");
				String LEVEL_UPTO = UINavigatorUtil.getI18nString("emxEngineeringCentral.Part.WhereUsedLevelUpTo", "emxEngineeringCentralStringResource", "en");
				String LEVEL_HIGHEST = UINavigatorUtil.getI18nString("emxEngineeringCentral.Part.WhereUsedLevelHighest", "emxEngineeringCentralStringResource", "en");
				String REV_LATEST_RELEASED = UINavigatorUtil.getI18nString("emxEngineeringCentral.Part.WhereUsedRevisionLatestReleased", "emxEngineeringCentralStringResource", "en");
				String LEVEL_UPTO_AND_HIGHEST = UINavigatorUtil.getI18nString("emxEngineeringCentral.Part.WhereUsedLevelUpToAndHighest", "emxEngineeringCentralStringResource", "en");
				boolean boolAddEndItemsToList = false;
				//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - start				
				ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
				isContextPushed = true;	
				//StringList objectSelect = createStringList(new String[] {DomainConstants.SELECT_ID, SELECT_PART_MODE, SELECT_RAISED_AGAINST_ECR,
				//		SELECT_PART_TO_ECR_CURRENT, SELECT_PART_TO_ECO_CURRENT, REL_TO_EBOM_EXISTS, SELECT_RAISED_AGAINST_ECR_CURRENT});				
				StringList objectSelect = createStringList(new String[] {DomainConstants.SELECT_ID});
				//DSM (DS) 2015x.5 - ALM 16822 - Phase row should not be checked - START
				objectSelect.add(SELECT_TYPE);
				//DSM (DS) 2015x.5 - ALM 16822 - Phase row should not be checked - END			
				
				//StringList relSelect = createStringList(new String[] {DomainConstants.SELECT_LEVEL, DomainConstants.SELECT_FIND_NUMBER,
				//							DomainConstants.SELECT_ATTRIBUTE_REFERENCE_DESIGNATOR, DomainRelationship.SELECT_NAME,
				//							DomainConstants.SELECT_ATTRIBUTE_QUANTITY, DomainRelationship.SELECT_ID});
				//StringList relSelect = createStringList(new String[] {DomainConstants.SELECT_LEVEL, DomainRelationship.SELECT_NAME,	DomainRelationship.SELECT_ID});
				StringList relSelect = null;
				//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - End
			boolean isECCInstalled = FrameworkUtil.isSuiteRegistered(context, "appVersionEngineeringConfigurationCentral", false, null, null);
				/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - Modified for adding a filter in the Where Used page to display Obsolete Where-Used relations  - START */
				String objectWhere = "";
				if(UTILS.isOfDSOOrigin(context, objectId))
				{
					objectWhere = "(revision == 'last' && current != '" + DomainConstants.STATE_PART_OBSOLETE + "')";
				}
				else
				{
					objectWhere = "revision == 'last'";
				}
				/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - Modified for adding a filter in the Where Used page to display Obsolete Where-Used relations  - End */
				if (REV_ALL.equals(strSelectedRevisions)) {
					objectWhere = null;
					if (isECCInstalled) {
						String STATE_SUPERSEDED = FrameworkUtil.lookupStateName(context, POLICY_CONFIGURED_PART, "state_Superseded");
						objectWhere = "current != '" + STATE_SUPERSEDED + "'";
					}
				} else if (REV_LATEST_RELEASED.equals(strSelectedRevisions)) {
					objectWhere = "(current == '" + DomainConstants.STATE_PART_RELEASE + "' && (revision == 'last' || next.current != '" + DomainConstants.STATE_PART_RELEASE + "'))";
				/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - Modified for adding a filter in the Where Used page to display Obsolete Where-Used relations  - START */
				} else if (DomainConstants.STATE_PART_OBSOLETE.equals(strSelectedRevisions)) {
					objectWhere = "current == '" + DomainConstants.STATE_PART_OBSOLETE + "'";
				}
				/*DSO 2013x.4 -  Where Used Enhancements for Relationship Matrix - Modified for adding a filter in the Where Used page to display Obsolete Where-Used relations  - End */
				Short shRecurseToLevel = 1;
				if (LEVEL_HIGHEST.equals(strSelectedLevel)) {
					shRecurseToLevel = -1;
				} else if (LEVEL_ALL.equals(strSelectedLevel)) {
					shRecurseToLevel = 0;
				} else if ((LEVEL_UPTO.equals(strSelectedLevel) || LEVEL_UPTO_AND_HIGHEST.equals(strSelectedLevel)) && isValidData(strSelectedLevelValue)) {
					shRecurseToLevel = Short.parseShort(strSelectedLevelValue);
				}
				//DSM 2015x.1 - External Security - START
				${CLASS:pgDSMSecurity} PGDSMSECURTY_INSTANCE = new ${CLASS:pgDSMSecurity}(context, args);
				if(PGDSMSECURTY_INSTANCE.isEBPUser(context, args))
					objectWhere = "";
				//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - Commented duplicate initialization - start
				//domObj = DomainObject.newInstance(context, objectId);
				//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue - start	
				
				String strCompiledBinaryCode = getCompiledBinaryCode(context, strEffectivityOID);
				//Modified by DSM (DS) 2015X.2 - Rel Matrix Starting materials Where Used - START
				partWhereUsedEBOMList = domObj.getRelatedObjects(context,
						//DSM (DS) 2015x.5.1 ALM 17562 JRH FPP to AFPP where used not working - STARTS
																	DomainConstants.RELATIONSHIP_EBOM+","+${CLASS:pgDSOConstants}.REL_STARTING_MATERIAL+","+${CLASS:pgDSOConstants}.REL_PG_AffectedFPPLIST,
																	DomainConstants.TYPE_PART+","+${CLASS:pgDSOConstants}.TYPE_PG_AffectedFPPLIST,
																	//DSM (DS) 2015x.5.1 ALM 17562 JRH FPP to AFPP where used not working - ENDS
																	objectSelect,
																	relSelect,
																	true,
																	false,
																	shRecurseToLevel,
																	objectWhere,
																	null,
																	(short) 0,
																	false,
																	false,
																	(short) 0,
																	null, null, null, null, strCompiledBinaryCode);
				//Modified by DSM (DS) 2015X.2 - Rel Matrix Starting materials Where Used - END
				if (!isValidData(strSelectedRefDes) && !isValidData(strSelectedFN)) {
					spareSubAltPartList = getSpareSubAltPartList(context, domObj, objectSelect, relSelect, objectWhere);
				}
				if (LEVEL_UPTO_AND_HIGHEST.equals(strSelectedLevel)) {
					boolAddEndItemsToList = true;
					//Modified by DSM (DS) 2015X.2 - Rel Matrix Starting materials Where Used - START
					endNodeList = domObj.getRelatedObjects(context,
							//DSM (DS) 2015x.5.1 ALM 17562 JRH FPP to AFPP where used not working - STARTS
																DomainConstants.RELATIONSHIP_EBOM+","+${CLASS:pgDSOConstants}.REL_STARTING_MATERIAL+","+${CLASS:pgDSOConstants}.REL_PG_AffectedFPPLIST,
																DomainConstants.TYPE_PART+","+${CLASS:pgDSOConstants}.TYPE_PG_AffectedFPPLIST,
																//DSM (DS) 2015x.5.1 ALM 17562 JRH FPP to AFPP where used not working - ENDS
																objectSelect,
																relSelect,
																true,
																false,
																(short) -1,
																objectWhere,
																null,
																(short) 0,
																false,
																false,
																(short) 0,
																null, null, null, null, strCompiledBinaryCode);
					//Modified by DSM (DS) 2015X.2 - Rel Matrix Starting materials Where Used - END
	            }
		
				/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - Modified to avoid MFG Installation check if the Object is DSO to display Substitite parts in Where Used Page  - START */
				
				if(UTILS.isOfDSOOrigin(context, objectId))
				{
					//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
					if (shRecurseToLevel != 1) {
						//for any refinement level other than default (i.e 1), invoke custom implementation for EBOM substitute where used
						ebomSubstituteList = getEbomSustituteParts(context, objectId, objectSelect, relSelect, objectWhere, shRecurseToLevel);
						// DSM(DS) 2018x.1.1 - ALM#24567 - INC2544282 - Where-used expansion with filter does not show eBOM substitutes at levels >=2. - Start
						MapList partWhereUsedFinalList = new MapList();
						if(partWhereUsedEBOMList!=null && !partWhereUsedEBOMList.isEmpty()) {
							for (Object partWhereUsedEBOMObj : partWhereUsedEBOMList) {
								Map partWhereUsedEBOMMap = (Map) partWhereUsedEBOMObj;
								partWhereUsedFinalList.add(partWhereUsedEBOMMap);
								String level = (String) partWhereUsedEBOMMap.get(DomainConstants.SELECT_LEVEL);
								//Level is 1 and expand level is 2, then substitute level should be 1 to be passed.
								short objLvl = Short.parseShort(level);
								if(shRecurseToLevel-objLvl>0) {
									short lvl = (short)(shRecurseToLevel-objLvl);
									MapList tmpList = null;
									if(lvl != 0) {
										tmpList = getEbomSustituteParts(context, (String) partWhereUsedEBOMMap.get(DomainConstants.SELECT_ID), objectSelect, relSelect, objectWhere,lvl);
									}

									if (tmpList != null) {
										getMappedLevelForList(tmpList,objLvl);
										partWhereUsedFinalList.addAll(tmpList);
									}
								}
							}
						}
						partWhereUsedEBOMList = partWhereUsedFinalList;
						// DSM(DS) 2018x.1.1 - ALM#24567 - INC2544282 - Where-used expansion with filter does not show eBOM substitutes at levels >=2. - End
					} else {
					//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS
					ebomSubstituteList = getEbomSustituteParts(context, objectId, objectSelect, relSelect, objectWhere);
					//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
					}
					//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS
				}else
				{
					if ("true".equalsIgnoreCase(strEBOMSubstitute)) { // Only if MFG is installed strEBOMSubstitute can be true.
	            	//Modified for IR-119203V6R2012x for getting the whereused parts according to the revision filter
						ebomSubstituteList = getEbomSustituteParts(context, objectId, objectSelect, relSelect, objectWhere);
					}
				}	
				/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - Modified to avoid MFG Installation check if the Object is DSO to display Substitite parts in Where Used Page  - End */
	            finalListReturn = mergeList(partWhereUsedEBOMList, endNodeList, spareSubAltPartList, ebomSubstituteList, boolAddEndItemsToList, strSelectedRefDes, strSelectedFN);
				/*DSO 2013x.4 -Where Used Enhancements for Relationship Matrix - For a Transport Unit, Where Used displays the FPP to which it is connected by the Transport Unit relationship.- START */
				if(UTILS.isOfDSOOrigin(context, objectId))
				{
					tupWhereUsedList = domObj.getRelatedObjects(context,
										${CLASS:pgDSOConstants}.REL_PG_TRANSPORT_UNIT,
										DomainConstants.TYPE_PART,
										objectSelect,
										relSelect,
										true,
										false,
										shRecurseToLevel,
										objectWhere,
										null,
										(short) 0,
										false,
										false,
										(short) 0,
										null, null, null, null, strCompiledBinaryCode);
					int iTUPListSize   = getListSize(tupWhereUsedList);
					Map map;
					String strLevel;
					for (int i = 0; i < iTUPListSize; i++) {
						map = (Map) tupWhereUsedList.get(i);
						strLevel = getStringValue(map, "level");
						map.put("objectLevel", strLevel);
						finalListReturn.add(map);
					}
				}	
			/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - For a Transport Unit, Where Used displays the FPP to which it is connected by the Transport Unit relationship.- End */		
			
				//DSM (DS) 2015x.2 - Where Used for Substitutes defined in Formulation Process - START
				String strPhaseId = null;
				String strLevel = null;
				
				//Modified for 2018x Upgrade. Rel Name PLBOM Substitute --> Modified to FormulationRelationship.FBOM_SUBSTITUTE STARTS

				//objectSelect.add(FormulationUtil.strcat("to[",FormulationRelationship.PLBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));
	    		objectSelect.add(FormulationUtil.strcat("to[",FormulationRelationship.FBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));
				//Modified for 2018x Upgrade. Rel Name PLBOM Substitute --> Modified to FormulationRelationship.FBOM_SUBSTITUTE ENDS
	    		
	    		MapList ParentSubList = domObj.getRelatedObjects(context,
	    				//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -starts
	    				${CLASS:pgDSOConstants}.REL_FBOM+","+FormulationRelationship.PLANNED_FOR.getRelationship(context)+","+FormulationRelationship.FORMULATION_PROPAGATE.getRelationship(context),
	    				//DSM (DS) 2018.0 FD02 Relationship change PLBOM to FBOM -ends	
	    				//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - start
	    		                                //FormulationType.PARENT_SUB.getType(context)+","+FormulationType.FORMULATION_PROCESS.getType(context)+","+FormulationType.FORMULATION_PART.getType(context)+","+FormulationType.COSMETIC_FORMULATION.getType(context),
							FormulationType.FORMULATION_PHASE.getType(context) + "," + FormulationType.PARENT_SUB.getType(context)+","+FormulationType.FORMULATION_PROCESS.getType(context)+","+FormulationType.FORMULATION_PART.getType(context)+","+FormulationType.COSMETIC_FORMULATION.getType(context),
	    		                                //DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - End
												objectSelect,
	    		                                relSelect,
	    										true,
	    										false,
											//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - start
	    										(short)1,
											//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - End
	    										"",
	    										"",
	    		                                0); 
	    		Iterator ParentSubIterator = ParentSubList.iterator();
				//Added by DSM (DS) for ALM Defect 9224 STARTS
				//Class cast exception on display of Where Used from RMP - Multiple connections for a PLBOM Substitute causing issue
				StringList slPhaseIds = new StringList(1);
				Iterator itrPhaseId;
				//Added by DSM (DS) for ALM Defect 9224 ENDS
	    		while(ParentSubIterator.hasNext())
	    		{
	    			Map ParentSubMap = (Map)ParentSubIterator.next();
	    			strLevel = getStringValue(ParentSubMap, DomainConstants.SELECT_LEVEL);
	    			ParentSubMap.put("objectLevel", strLevel);
	    			if(FormulationType.PARENT_SUB.getType(context).equals(getStringValue(ParentSubMap, DomainConstants.SELECT_TYPE)))
	    			{
						//Added by DSM (DS) for ALM Defect 9224 STARTS
						//Class cast exception on display of Where Used from RMP - Multiple connections for a PLBOM Substitute causing issue
	    				//strPhaseId = (String) ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.PLBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id")); 
	    				
	    				//Modified for 2018x Upgrade. Rel Name PLBOM Substitute --> Modified to FormulationRelationship.FBOM_SUBSTITUTE STARTS
	    				//Object oTempObj = ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.PLBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));
						Object oTempObj = ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.FBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));
						//Added for 2018x Upgrade ALM 20589 - Added if condition to check object oTempObj value is empty incase of substitude rel is not there for FBOM - Starts 
						if(oTempObj != null) {
						//Added for 2018x Upgrade ALM 20589 - Added if condition to check object oTempObj value is empty incase of substitude rel is not there for FBOM - Ends
							if (oTempObj.getClass() == StringList.class) {
								//slPhaseIds = (StringList) ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.PLBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));  
								slPhaseIds = (StringList) ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.FBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));
								//Modified for 2018x Upgrade. Rel Name PLBOM Substitute --> Modified to FormulationRelationship.FBOM_SUBSTITUTE ENDS

								if(slPhaseIds != null && slPhaseIds.size() > 0) {
									itrPhaseId = slPhaseIds.iterator();
									while (itrPhaseId.hasNext()) {
										strPhaseId = (String)itrPhaseId.next();						
										ParentSubMap.put(DomainConstants.SELECT_ID, strPhaseId);
									}
								}
							} else {
							//Added by DSM (DS) for ALM Defect 9224 ENDS
								
							//Modified for 2018x Upgrade. Rel Name PLBOM Substitute --> Modified to FormulationRelationship.FBOM_SUBSTITUTE STARTS
							//strPhaseId = (String) ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.PLBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id"));	
							strPhaseId = (String) ParentSubMap.get(FormulationUtil.strcat("to[",FormulationRelationship.FBOM_SUBSTITUTE.getRelationship(context),"].fromrel.from.id")); 
							//Modified for 2018x Upgrade. Rel Name PLBOM Substitute --> Modified to FormulationRelationship.FBOM_SUBSTITUTE ENDS

							ParentSubMap.put(DomainConstants.SELECT_ID, strPhaseId);
							//Added by DSM (DS) for ALM Defect 9224 STARTS
							}
							//Added by DSM (DS) for ALM Defect 9224 ENDS
                     	//Added for 2018x Upgrade ALM 20589 - Added if condition to check object oTempObj value is empty incase of substitude rel is not there for FBOM - starts
						} else {
		    				continue;
		    			}
						//Added for 2018x Upgrade ALM 20589 - Added if condition to check object oTempObj value is empty incase of substitude rel is not there for FBOM - Ends
	    			}
	    			
					finalListReturn.add(ParentSubMap);
	    		}
	    		
	    		//DSM (DS) 2015x.2 - Where Used for Substitutes defined in Formulation Process - END
				
			}
		}
		/*DSM2015x - Added changes for DSM15X2-10 : Disable selection of next level parts on Where Used table - Start*/
			//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - start
		//MapList mlCustomList = new MapList();
		//int iCount = 0;
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - End
		for(Object obj:finalListReturn)
		{
			Map mpObjectMap = (Map)obj;
			//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - start
			String strRelName = (String)mpObjectMap.get("relationship");
			//String strObjectId = mpObjectMap.get("id").toString();
			//if(UIUtil.isNotNullAndNotEmpty(strObjectId) && !slParentId.contains(strObjectId))
			//DSM (DS) 2015x.5 - ALM 16822 - Phase row should not be checked - START
			strType = (String)mpObjectMap.get(SELECT_TYPE);
			if(!(objectId.equalsIgnoreCase(strParentOID) && strRelName.equalsIgnoreCase(DomainConstants.RELATIONSHIP_EBOM)) || 
					(DomainConstants.RELATIONSHIP_EBOM.equals(strRelName) && UIUtil.isNotNullAndNotEmpty(strType) && pgV3Constants.TYPE_PGPHASE.equals(strType)))
			//DSM (DS) 2015x.5 - ALM 16822 - Phase row should not be checked - END
			{
			//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - End
				mpObjectMap.put("disableSelection", "true");
			}
			mlCustomList.add(mpObjectMap);
		}
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - start
		//return mlCustomList;
		/*DSM2015x - Added changes for DSM15X2-10 : Disable selection of next level parts on Where Used table - End*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			throw ex;			
		}
		finally
		{
			if(isContextPushed)
			{
				ContextUtil.popContext(context);
			}
		}
		
		return mlCustomList;
		//DSM DS 2015x.4 ALM 14496 - Code modification for performance issue  - End
	}

	  /**DSM(DS) 2018x.1.1 - ALM#24567 - INC2544282 - Where-used expansion with filter does not show eBOM substitutes at levels >=2.
	   * getMappedLevelForList will adjust the level according to the parent level passed.
	   * @param tmpList
	   * @param lvl
	   */
	  private void getMappedLevelForList(MapList tmpList, short lvl) {
	  	for(Object tmpObj: tmpList) {
	  		Map tmpMap = (Map)tmpObj;
			String level = (String) tmpMap.get(DomainConstants.SELECT_LEVEL);
			short adjLvl = (short) (lvl+ Short.parseShort(level));
			tmpMap.put(DomainConstants.SELECT_LEVEL,String.valueOf(adjLvl));
		}
	  }

		/* Overridden from emxECPartBase */
		
		/** gets all the Spare, Alternate, Substitute parts connected to it.
	 * @param context ematrix context
	 * @param domObj current part domainobject instance
	 * @param objectSelect StringList object select statements
	 * @param relSelect StringList relationship select statements
	 * @param objectWhere where condition to apply on objects
	 * @return MapList
	 * @throws Exception if any exception occurs.
	 */
	public MapList getSpareSubAltPartList(Context context, DomainObject domObj, StringList objectSelect, StringList relSelect, String objectWhere) throws Exception {
		String relSpareSubAlternate = DomainConstants.RELATIONSHIP_ALTERNATE + "," + DomainConstants.RELATIONSHIP_SPARE_PART;
		MapList whereUsedSpareSubAltList = domObj.getRelatedObjects(context,
																		relSpareSubAlternate,
																		DomainConstants.QUERY_WILDCARD,
																		objectSelect,
																		relSelect,
																		true,
																		false,
																		(short) 1,
																		objectWhere,
																		null, (short)0);
		int size = getListSize(whereUsedSpareSubAltList);
		MapList listReturn = new MapList(size);
		Map map;
		String type;
		String subPartId;
		String substituteRel = RELATIONSHIP_STANDARD_COMPONENT + "," + RELATIONSHIP_COMPONENT_SUBSTITUTION;
		MapList subpartList;
		DomainObject tempDomObject;
		for (int i = 0; i < size; i++) {
			map = (Map) whereUsedSpareSubAltList.get(i);
				listReturn.add(map);
		}
		return listReturn;
	}
	
	/**
	 * DSO : Overridden from emxECPartBase
	 * Merges the MapList whereUsed, EndNode, Substitute parts
	 * @param whereUsedList
	 * @param endItemList
	 * @param spareSubAltPartList
	 * @param ebomSubList
	 * @param boolAddEndItemsToList
	 * @param refDesFilter
	 * @param fnFilter
	 * @return MapList
	 */
	
	public MapList mergeList(MapList whereUsedList, MapList endItemList, MapList spareSubAltPartList, MapList ebomSubList,boolean boolAddEndItemsToList, String refDesFilter, String fnFilter) {
		int iWhereUsedListSize = getListSize(whereUsedList);
		int iEndItemListSize   = getListSize(endItemList);
		int iEbomSubListSize   = getListSize(ebomSubList);
		int iSpareSubAltSize   = getListSize(spareSubAltPartList);
        StringList sListEndItemId = getDataForThisKey(endItemList, DomainConstants.SELECT_ID);
		MapList listReturn = new MapList(iWhereUsedListSize);
        Map map;
		String objectId;
		String strLevel;
		String strRelEBOMExists;
		for (int i = 0; i < iWhereUsedListSize; i++) {
			map = (Map) whereUsedList.get(i);
			objectId = getStringValue(map, DomainConstants.SELECT_ID);
            if (isFNAndRefDesFilterPassed(map, refDesFilter, fnFilter)) {
                strLevel = getStringValue(map, "level");
                map.put("objectLevel", strLevel);
                strRelEBOMExists = getStringValue(map, REL_TO_EBOM_EXISTS);
                if ("False".equals(strRelEBOMExists)) {
                    map.put("EndItem", "Yes");
                    sListEndItemId.remove(objectId);
                }
                listReturn.add(map);
            }
        }
		for (int i = 0; i < iEbomSubListSize; i++) {
			map = (Map) ebomSubList.get(i);
        	if (isFNAndRefDesFilterPassed(map, refDesFilter, fnFilter)) {
                strLevel = getStringValue(map, "level");
                map.put("objectLevel", strLevel);
				//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
				if(strLevel.equals("1")) {
				//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS
                map.put("relationship", RELATIONSHIP_EBOM_SUBSTITUTE);
				//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
				}
				//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS
                listReturn.add(map);
        	}
        }
		if (boolAddEndItemsToList) {
            for (int i = 0; i < iEndItemListSize; i++) {
                map = (Map) endItemList.get(i);
                objectId = getStringValue(map, DomainConstants.SELECT_ID);
            	if (sListEndItemId.contains(objectId) && isFNAndRefDesFilterPassed(map, refDesFilter, fnFilter)) {
                    map.put("EndItem", "Yes");
                    listReturn.add(map);
                }
            }
        }
		for (int i = 0; i < iSpareSubAltSize; i++) {
			map = (Map) spareSubAltPartList.get(i);
            strLevel = getStringValue(map, "level");
            map.put("objectLevel", strLevel);
            listReturn.add(map);
        }
		return listReturn;
	}
	
	/**
	 * DSO : Overridden from emxECPartBase
	 * Iterates through MapList and Returns the values in StringList depending upon key
	 * @param list
	 * @param key
	 * @return
	 */
	public StringList getDataForThisKey(MapList list, String key) {
		int size = getListSize(list);
		StringList listReturn = new StringList(size);
		String strTemp;
		for (int i = 0; i < size; i++) {
			strTemp = (String) ((Map) list.get(i)).get(key);
			if (!isValidData(strTemp)) {
				strTemp = "";
			}
			listReturn.addElement(strTemp);
		}
		return listReturn;
	}
	
	/**
	 * DSO : Overridden from emxECPartBase to retrieve the size of List object
	 * @param list
	 * @return
	 */
	
	public int getListSize(List list) {
		return list == null ? 0 : list.size();
	}
	
	/**
	
	 * DSO : If user has entered some valid value in Ref Des OR Findnumber it compares with attributes exists in map and if both are same only then it returns true,
	 * if both findNumber, refDes == NUll or "", then this method returns true.
	 * @param map contains properties of object like id, name, rel attributes, object attributes.
	 * @param refDes value given by user from UI in Reference Designator textfield.
	 * @param findNumber value given by user from UI in Find Number textfield.
	 * @return boolean.
	 */
	public boolean isFNAndRefDesFilterPassed(Map map, String refDes, String findNumber) {
		boolean boolRefDesFilterPass = true;
		boolean boolFNFilterPass = true;
		String strRefDes = getStringValue(map, DomainConstants.SELECT_ATTRIBUTE_REFERENCE_DESIGNATOR);
		String strFindNumber = getStringValue(map, DomainConstants.SELECT_ATTRIBUTE_FIND_NUMBER);
		if (isValidData(refDes) && !refDes.equals(strRefDes)) {
            boolRefDesFilterPass = false;
        }
        if (isValidData(findNumber) && !findNumber.equals(strFindNumber)) {
            boolFNFilterPass = false;
        }
		return (boolRefDesFilterPass && boolFNFilterPass);
	}
	
	/**
	 * DSO : Overridden from emxECPartBase and modified for Where Used Enhancements
	 * Returns HashMap containing values of Revisions in English and Context lang to display in filter
	 * @param context the eMatrix <code>Context</code> object.
	 * @param args holds no arguments.
	 * @throws Exception if the operation fails.
	 */	
	public HashMap getRevisionsFilterForPartWhereUsed(Context context, String[] args) throws Exception {
		/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix -Modified for adding a filter in the Where Used page to display Obsolete Where-Used relations  - START */
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
		HashMap requestMap =(HashMap)programMap.get("requestMap");
		String objectId = (String) requestMap.get("objectId");
		String[] revisionOptions;
		if(UTILS.isOfDSOOrigin(context, objectId)) {
			revisionOptions = new String[4];
			revisionOptions[0] = "emxEngineeringCentral.Part.WhereUsedRevisionLatest";
			revisionOptions[1] = "emxEngineeringCentral.Part.WhereUsedRevisionLatestReleased";
			revisionOptions[2] = "emxEngineeringCentral.Part.WhereUsedRevisionAll";
			revisionOptions[3] = "emxEngineeringCentral.Part.WhereUsedStateObsolete";
		}
		else
		{
			revisionOptions = new String[3];
			revisionOptions[0] = "emxEngineeringCentral.Part.WhereUsedRevisionLatest";
			revisionOptions[1] = "emxEngineeringCentral.Part.WhereUsedRevisionLatestReleased";
			revisionOptions[2] = "emxEngineeringCentral.Part.WhereUsedRevisionAll";
		}
		/*DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - Modified for adding a filter in the Where Used page to display Obsolete Where-Used relations  - End */
		HashMap revisionMap = new HashMap(2);
		revisionMap.put("field_choices", getValueListFromProperties(revisionOptions, "emxEngineeringCentralStringResource", "en"));
		revisionMap.put("field_display_choices", getValueListFromProperties(revisionOptions, "emxEngineeringCentralStringResource", context.getSession().getLanguage()));
		return revisionMap;
	}
	/**
	 * DSO : Overridden from emxECPartBase
	 * @param context the eMatrix <code>Context</code> object.
	 * @param args holds no arguments.
	 * @throws Exception if the operation fails.
	 */
	public StringList getValueListFromProperties(String[] objSizeArr, String resource, String languageStr) throws Exception {
    	 int length = length (objSizeArr);
    	 StringList list = new StringList(length);
    	 String temp;
    	 for (int i = 0; i < length; i++) {
    		 temp = UINavigatorUtil.getI18nString(objSizeArr[i], resource, languageStr);
    		 list.add(temp);
    	 }
    	 return list;
     }
	/* DSO 2013x.4 - Where Used Enhancements for Relationship Matrix - End*/
	
	/* DSO 2013x.4 -  Stages for Relationship Matrix - Start*/
	
	/**
	 * This method is use as check trigger on EBOM Substitute relation ship.
	 * This method is use to check the rule
	 * 1. If a Parent is at the Production Stage all Substitute Part must be at the Production stage.
         * 2. If the Parent is at the Pilot stage all Substitute Part must be in Pilot or Production stage.
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	
	public int checkStageOfSubstitutePart(Context context, String[] args) throws Exception{
	int iReturn = 0;
	try{
		
		String strAllowedStages = "";
		if(args != null && args.length > 0){
		
			String strSubPartid = args[0];
			String stEBOMid[] = {args[1]};
			//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - STARTS
			String strType = DomainConstants.EMPTY_STRING;
			//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - ENDS
			
	        StringList relSelect = new StringList();
	        relSelect.add(DomainObject.SELECT_TO_ID);
	      //DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - STARTS
	        relSelect.add(DomainObject.SELECT_TO_TYPE);
	      //DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - ENDS
	        MapList parentPartIdMap = DomainRelationship.getInfo(context, stEBOMid, relSelect);
	        String strParentId = (String) ((Map) parentPartIdMap.get(0)).get(DomainObject.SELECT_TO_ID);
	      //DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - STARTS
	        strType = (String) ((Map) parentPartIdMap.get(0)).get(DomainObject.SELECT_TO_TYPE);
	      //DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - ENDS
	        if(UIUtil.isNotNullAndNotEmpty(strParentId) && UIUtil.isNotNullAndNotEmpty(strSubPartid))
	        {
				DomainObject domParent = DomainObject.newInstance(context,strParentId);
				DomainObject domSubPart  = DomainObject.newInstance(context,strSubPartid);
				//JPO Compilation Fixing 2015XUPGRADE START
				//DSM (DS) 2015x.1 ALM 6622: Structure Copy is failing : EBP Scenario  - START
				ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
				//Modified for 2018x Upgrade  Starts - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
				String strParentStageValue = domParent.getInfo(context,${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
				String strSubPartStageValue = domSubPart.getInfo(context,${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
				//Modified for 2018x Upgrade Ends - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
				ContextUtil.popContext(context);
				//DSM (DS) 2015x.1 ALM 6622: Structure Copy is failing : EBP Scenario  - End
				//JPO Compilation Fixing 2015XUPGRADE END
				String errorMessage= i18nNow.getI18nString("emxCPN.CreateEBOMSubstitute.InvalidStage.Error.Alert","emxCPNStringResource",context.getSession().getLanguage());
							
				if(UIUtil.isNotNullAndNotEmpty(strParentStageValue) && UIUtil.isNotNullAndNotEmpty(strSubPartStageValue)){
					//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - STARTS
					if(UIUtil.isNotNullAndNotEmpty(strType) && ${CLASS:pgDSOConstants}.TYPE_FORMULATION_PART.equalsIgnoreCase(strType) && ${CLASS:pgDSOConstants}.FORMULATION_PART_EXPERIMENTAL_RELEASEPHASE.equalsIgnoreCase(strParentStageValue))
					{
						strAllowedStages = FrameworkProperties.getProperty(context,"emxCPN.CreatePDBOM."+strParentStageValue+".FormulationPart.AllwedStages");
					}
					else {
						//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - ENDS
					strAllowedStages = FrameworkProperties.getProperty(context,"emxCPN.CreatePDBOM."+strParentStageValue+".AllwedStages");
					}
					//DSM (DS) 2018x.0 ALM 24138 - Not able to add the FOP under COP - ENDS
						
					if(!strAllowedStages.contains(strSubPartStageValue)){
						iReturn = 1;						
						${CLASS:emxContextUtil}.mqlNotice(context, errorMessage + strAllowedStages);
					}					
				}
	        }
		}
		}catch(Exception e){
			e.printStackTrace();
			return 1;
		}
		return iReturn;
	}
	
	
	
	//DSO 2013x.4 , added for RalatinoShip Matrix-Rev3 Stages, When the Parent is in the same Stage as the Children Transport Unit the child must be in a higher/same lifecycle State as that of the Parent. -Start
	//Overridden and Modified from the Base
		/**
      * This method checks if the children objects related
      * to the parent with the specified relationships
      * with the "to" direction have
      * reached the target state given.
      *
      * The intent of this program is to provide a function
      * which checks the state of all objects of
      * a named object type related to a parent object.
      * The returned value will inform the parent if all the
      * requested related objects are at a given state so
      * that the parent can be promoted to the next state.
      *
      * @param context the eMatrix <code>Context</code> object
      * @param args hold the following input arguments:
      *
      *
      *  args[0] - sRelationship  -args[0] - Relationship to expand from, mutltiple relationships
      *                                      can be entered as a string delimited with spaces(" "), "~" or ",". (Optional) (default "*")
      *                                      Ex. relationship_PartSpecification,relationship_DrawingSpecification
      *                                      Passing in one of the following will expand on all
      *                                      relationships:  * or "" (NULL).
      *
      *  args[1] - sTargetObject  - args[1] - Object to expand on, multiple objects can be entered
      *                                       as a string, delimited with spaces(" "), "~" or ",". (Optional) (default "*")
      *                                       Ex. type_Part,type_DrawingPrint
      *                                       Passing in one of the following will expand on all
      *                                       objects:  * or "" (NULL).
      *
      *  args[2] - sTargetStateProp - The state being checked for. Symbolic name must be used. (Optional)
      *  args[3] - sDirection            - The direction to expand.  Valid entries are "from" or "to".
      *                                            (Optional) (default both to and from).
      *
      *  args[4] - sComparisonOperator   -  Operator to check state with. Valid
      *                                                   entries are LT, GT, EQ, LE, GE, and NE. (Optional) (default - "EQ")
      *  args[5] - sObjectRequired    -  Set "required" flag if an object should be connected. Valid entries
      *                                              are Required and Optional. (Optional) (default - "Optional").
      *  args[6] - sStateRequired     -  Set "required" flag if target state should be present. Valid entries
      *                                             are Required and Optional. (Optional) (default - "Required")
      *
      * @return  0 if all children are in a valid state.
      *          1 if any child is in an invalid state.
      * @throws Exception if the operation fails
      * @since EC 10.6.SP2
      */
      public int checkRelatedObjectState(Context context, String []args)  throws Exception
      {
          String strOutput = "";
          int intOutput = 0;
          // Create an instant of emxUtil JPO
          ${CLASS:emxUtil} utilityClass = new ${CLASS:emxUtil}(context, null);
          // Get Required Environment Variables
          String arguments[] = new String[1];
          arguments[0] = "get env OBJECTID";
          ArrayList cmdResults = utilityClass.executeMQLCommands(context, arguments);
          String sObjectId = (String)cmdResults.get(0);
          StringBuffer sBuffer = new StringBuffer();
          String sRel = "";
          String sTargObject = "";
          String sRelationship    = args[0];
          String sTargetObject    = args[1];
          String sTargetStateProp = args[2];
          String sDirection       = args[3];
          String sComparisonOperator  = args[4];
          String sObjectRequired      = args[5];
          String sStateRequired       = args[6];
          // If no value for operator set it to EQ
          if("".equals(sComparisonOperator))
          {
              sComparisonOperator = "EQ";
          }
          // If value for Object Required in not Required set it to Optional
          if(!"required".equalsIgnoreCase(sObjectRequired))
          {
              sObjectRequired = "Optional";
          }
          // If value for State Required in not Required set it to Optional
          if(!"optional".equalsIgnoreCase(sStateRequired))
          {
              sStateRequired = "Required";
          }
          StringTokenizer strToken = new StringTokenizer(sRelationship, " ,~");
          String strRel = "";
          String strRelRealName = "";
          while(strToken.hasMoreTokens())
          {
              strRel = strToken.nextToken().trim();
              strRelRealName = PropertyUtil.getSchemaProperty(context,strRel);
              if("".equals(strRelRealName))
              {
                   // Error out if not registered
                  arguments = new String[5];
                  arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState_if.InvalidRel";
                  arguments[1] = "1";
                  arguments[2] = "Rel";
                  arguments[3] = strRel;
                  arguments[4] = "";
                  ${CLASS:emxMailUtil} mailUtil = new ${CLASS:emxMailUtil}(context, null);
                  strOutput = mailUtil.getMessage(context,arguments);
                  intOutput = 1;
                  break;
              }
              else
              {
                  if(sBuffer.length() > 0)
                  {
                      sBuffer.append(",");
                  }
                  sBuffer.append(strRelRealName);
              }
          }
          if(sBuffer.length() > 0)
          {
              sRel = sBuffer.toString();
          }
          else
          {
              // Set Relationship to * if one is not entered
              sRel = "*";
          }
          if(intOutput == 0)
          {
              sBuffer = new StringBuffer();
              strToken = new StringTokenizer(sTargetObject, " ,~");
              String sTypeResult = "";
              String sTypeRealName = "";
              while(strToken.hasMoreTokens())
              {
                  sTypeResult = strToken.nextToken().trim();
                  sTypeRealName = PropertyUtil.getSchemaProperty(context,sTypeResult);
                  if("".equals(sTypeRealName))
                  {
                      // Error out if not registered
                      arguments = new String[5];
                      arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState_if.InvalidType";
                      arguments[1] = "1";
                      arguments[2] = "Type";
                      arguments[3] = sTypeResult;
                      arguments[4] = "";
                      ${CLASS:emxMailUtil} mailUtil = new ${CLASS:emxMailUtil}(context, null);
                      strOutput = mailUtil.getMessage(context,arguments);
                      intOutput = 1;
                      break;
                  }
                  else
                  {
                      if(sBuffer.length() > 0)
                      {
                          sBuffer.append(",");
                      }
                      sBuffer.append(sTypeRealName);
                  }
              }
              if(sBuffer.length() > 0)
              {
                  sTargObject = sBuffer.toString();
              }
              else
              {
                  // Set Target Object to * if one is not entered
                  sTargObject = DomainConstants.QUERY_WILDCARD;
              }
          }
          if(intOutput == 0)
          {
              String sTargetState = "";
			  boolean processStatus = true;
              ${CLASS:emxMailUtil} mailUtil = new ${CLASS:emxMailUtil}(context, null);
              DomainObject domObj = DomainObject.newInstance(context,sObjectId);
            //JPO Compilation Fixing 2015XUPGRADE START
			//Modified for 2018x Upgrade - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
			  String sPartentStage = domObj.getInfo(context,${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
			//JPO Compilation Fixing 2015XUPGRADE END
              boolean bParentState = true;
              // If no Target state is defined use current state of object
              if(sTargetStateProp == null || "".equals(sTargetStateProp))
              {
                  sTargetState = domObj.getInfo(context,DomainConstants.SELECT_CURRENT);
                  bParentState = false;
              }
              // prepare getRelatedObjects parameters
              boolean getToRelationships = true;
              boolean getFromRelationships = true;
              if("to".equalsIgnoreCase(sDirection))
              {
                  getFromRelationships = false;
              }
              else if("from".equalsIgnoreCase(sDirection))
              {
                  getToRelationships = false;
              }
              // Only get the children that are not on the same ECO
              String ecoId = "";
              StringList selectRelStmts = new StringList();
              StringList selectStmts  = new StringList(1);
              selectStmts.addElement(SELECT_ID);
              MapList ecoMapList = getECO(context,selectStmts,selectRelStmts);
              if (ecoMapList.size() > 0)
              {
                  Map ecoMap = (Map)ecoMapList.get(0);
                  ecoId = (String)ecoMap.get(SELECT_ID);
              }
              String whereClause = "";
              if (ecoId != null && ecoId.length() > 0)
              {
                  whereClause = "to["+RELATIONSHIP_AFFECTED_ITEM+"] == False || to["+RELATIONSHIP_AFFECTED_ITEM+"].from.id!=\""+ecoId+"\"";
              }
              StringList strListObj = new StringList(6);
              strListObj.add(DomainConstants.SELECT_ID);
              strListObj.add(DomainConstants.SELECT_TYPE);
              strListObj.add(DomainConstants.SELECT_NAME);
              strListObj.add(DomainConstants.SELECT_REVISION);
              strListObj.add(DomainConstants.SELECT_CURRENT);
              strListObj.add(DomainConstants.SELECT_POLICY);
              //DSM(DS) - 2018x.1.1 - ALM 25859 - PRB0058825 - CA Blocked from Completion Due to FPP added as CUP Substitute - START
              //Removed whereClause from getRelatedObjects
              /*MapList mapList = domObj.getRelatedObjects(context,
                                                         sRel,
                                                         sTargObject,
                                                         strListObj,
                                                         null,
                                                         getToRelationships, // getTo relationships
                                                         getFromRelationships, // getFrom relationships
                                                         (short)1,
                                                         whereClause,
                                                         "",
                                                         (short)0);*/
              MapList mapList = domObj.getRelatedObjects(context,
                                                       sRel,
                                                       sTargObject,
                                                       strListObj,
                                                       null,
                                                       getToRelationships, // getTo relationships
                                                       getFromRelationships, // getFrom relationships
                                                       (short)1,
                                                       null,
                                                       "",
                                                       (short)0);
              
			  StringList slRelatedPartIds = BusinessUtil.toStringList(mapList, DomainConstants.SELECT_ID);
			  
			  if(BusinessUtil.isNotNullOrEmpty(slRelatedPartIds)){
				  StringList slPartIds = new StringList(slRelatedPartIds);
				  slPartIds.addElement(sObjectId);
				  //Getting CA details of parent and the related parts
				  Map mpPartCAPropInfo = ChangeUtil.getChangeObjectsInProposed(context,new StringList(SELECT_ID), slPartIds.toArray(new String[slPartIds.size()]), 1);
				  Map mpPartCARealInfo = ChangeUtil.getChangeObjectsInRealized(context,new StringList(SELECT_ID), slPartIds.toArray(new String[slPartIds.size()]), 1);
				  
				  MapList mlCAPropInfo = null ;
				  MapList mlCARealInfo = null ;
				  String strCAId = EMPTY_STRING;
				  
				  Map partCAMap = new HashMap();
				  
				  for(String partId: slPartIds){
					  strCAId = EMPTY_STRING;
					  mlCAPropInfo = (MapList)mpPartCAPropInfo.get(partId);
					  mlCARealInfo = (MapList)mpPartCARealInfo.get(partId);
					  
					  if(mlCAPropInfo.size()>0){
						  strCAId = ((Map) mlCAPropInfo.get(0)).get(SELECT_ID).toString();
					  } else if (mlCARealInfo.size()>0){
						  strCAId = ((Map) mlCARealInfo.get(0)).get(SELECT_ID).toString();
					  }
					  partCAMap.put(partId, strCAId);
				  }
				  
				  String strParentCAId = (String) partCAMap.remove(sObjectId) ;
				  String strChildPartId = EMPTY_STRING;
				  String strChildCAId = EMPTY_STRING;
				  MapList tmpMapList = new MapList();
				  //Checking and removing from the mapList if the related Part CA Id is same as that of parent CA part Id
				  for(int count=0 ;count<slRelatedPartIds.size();count++){
					  strChildPartId = slRelatedPartIds.get(count);
					  strChildCAId = (String) partCAMap.get(strChildPartId);
					  if(UIUtil.isNotNullAndNotEmpty(strParentCAId) && UIUtil.isNotNullAndNotEmpty(strChildCAId) && strChildCAId.equals(strParentCAId)){
						  tmpMapList.add((Map)mapList.get(count));
					  }
				  }
				  Iterator itr = tmpMapList.iterator();
				  Map tmpMap = new HashMap();
				  while (itr.hasNext()){
					  tmpMap = (Map) itr.next();
					  mapList.remove(tmpMap);
				  }
			  }
              //DSM(DS) - 2018x.1.1 - ALM 25859 - PRB0058825 - CA Blocked from Completion Due to FPP added as CUP Substitute - END
              int size = 0;
              if(mapList != null && (size = mapList.size()) > 0)
              {
                  // Create a list of all matching objects and check their state
                  Map map = null;
                  String sChildID = "";
                  String sChildType = "";
                  String sChildName = "";
                  String sChildRev = "";
                  String sChildCurrent = "";
                  String sChildPolicy = "";
				  String sChildStage = "";
                  StringList strListChildStates = null;
                  String sDvlpPartPolicy = DomainConstants.POLICY_DEVELOPMENT_PART;
                  for(int i = 0 ; i < size ; i++)
                  {
                      map = (Map)mapList.get(i);
                      sChildID = (String)map.get(DomainConstants.SELECT_ID);
                      sChildType = (String)map.get(DomainConstants.SELECT_TYPE);
                      sChildName = (String)map.get(DomainConstants.SELECT_NAME);
                      sChildRev = (String)map.get(DomainConstants.SELECT_REVISION);
                      sChildCurrent = (String)map.get(DomainConstants.SELECT_CURRENT);
                      sChildPolicy = (String)map.get(DomainConstants.SELECT_POLICY);
                	  //dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - Start
                      boolean isLastRevision = false;
                	  //dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - End
                      //DSM(DS) 2018x.2 - ALM 29199 - Unable to release PMP connected to POA in Review state - START
                      /* Comments from TCL eServicecommonCheckRelState.tcl
                       * 2015X.1 - Skip the state check if the connected part specification of type pngComponent (CATIA) or ArtiosCAD Component (Artios)
                       * 2015X.5 - 17016 - Skip the state check if the connected part specification of type POA
                       */
                      if(${CLASS:pgDSOConstants}.TYPE_PGCONFIGURATION.equals(sChildType) 
                    		  || ${CLASS:pgDSOConstants}.TYPE_ARTIOSCAD_COMPONENT.equals(sChildType)
                    		  || ${CLASS:pgDSOConstants}.TYPE_POA.equals(sChildType))
                      {
                    	  continue;
                      }
                      //DSM(DS) 2018x.2 - ALM 29199 - Unable to release PMP connected to POA in Review state - END
                      /*
                      *If a dvlp part, then we need to equate "Approved" (Common Part State) to
                      * "Complete" (Dvlp part state) and "Review" (Common Part State)
                      * to "Peer Review" (Dvlp Part State)
                      */
                      if(sChildPolicy.equals(sDvlpPartPolicy))
                      {
                          if("state_Approved".equals(sTargetStateProp))
                          {
                              sTargetStateProp = "state_Complete";
                          }
                          else if("state_Review".equals(sTargetStateProp))
                          {
                              sTargetStateProp = "state_PeerReview";
                          }
                      }
                      if(UIUtil.isNotNullAndNotEmpty(sChildID))
                      {
                    	  domObj.setId(sChildID);
                    	//JPO Compilation Fixing 2015XUPGRADE START
						//Modified for 2018x Upgrade - Stage attribute is replaced by Release Phase attribute in 18x OOTB. 
                    	  sChildStage 	= domObj.getInfo(context,${CLASS:pgDSOConstants}.SELECT_ATTRIBUTE_RELEASE_PHASE);
                    	//JPO Compilation Fixing 2015XUPGRADE END
                    	  //dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - Start
                    	  isLastRevision = domObj.isLastRevision(context);
                    	  //dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - End
                      }
					  if(${CLASS:pgDSOConstants}.TYPE_PG_TRANSPORTUNIT.equals(sChildType) && UIUtil.isNotNullAndNotEmpty(sChildStage) && UIUtil.isNotNullAndNotEmpty(sPartentStage)&& sChildStage.equals(sPartentStage))
					  {
						processStatus = true;
					  }
					  else
					  {
					    processStatus = false;
					  }
					  if(processStatus || !${CLASS:pgDSOConstants}.TYPE_PG_TRANSPORTUNIT.equals(sChildType))
					  {
					    //dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - Start
						// Get all states for object
						strListChildStates = domObj.getInfoList(context,DomainConstants.SELECT_STATES);
						/*dso2013x.5 - ALM:4880 - [Change Management]--Can't promote experimental MCOP to "Review" - Start*/
						 if(UIUtil.isNullOrEmpty(sTargetState))
						 {
							 sTargetState = PropertyUtil.getSchemaProperty(context,"policy",sChildPolicy,sTargetStateProp);
						 }
						/*dso2013x.5 - ALM:4880 - [Change Management]--Can't promote experimental MCOP to "Review" - End*/
						int indexTargetState = strListChildStates.indexOf(sTargetState);
						int indexChildState = strListChildStates.indexOf(sChildCurrent);
				    	//dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - End
						if(bParentState)
						{
							sTargetState = PropertyUtil.getSchemaProperty(context,"policy",sChildPolicy,sTargetStateProp);
					    	//dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - Start
							if((sTargetState == null || "".equals(sTargetState)) && (isLastRevision && indexChildState < strListChildStates.size()-2))
						    //dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - End
							{
								if("required".equalsIgnoreCase(sStateRequired))
								{
									// Error out if not registered
									arguments = new String[7];
									arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.InvalidState";
									arguments[1] = "2";
									arguments[2] = "State";
									arguments[3] = sTargetStateProp;
									arguments[4] = "Policy";
									arguments[5] = sChildPolicy;
									arguments[6] = "";
									intOutput = 1;
									strOutput = strOutput + mailUtil.getMessage(context,arguments);
									break;
								}
								else
								{
									continue;
								}
							}
						}
	
						// check if target state is in object's policy
						if(indexTargetState < 0)
						{
					    	//dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - Start
							if("required".equalsIgnoreCase(sStateRequired) && (isLastRevision && indexChildState < strListChildStates.size()-2))
					    	//dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - End
							{
								arguments = new String[13];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.InvalidTargetState";
								arguments[1] = "5";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "Policy";
								arguments[9] = sChildPolicy;
								arguments[10] = "State";
								arguments[11] = sTargetState;
								arguments[12] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
							continue;
						}
						// Get index location for object
						int index = strListChildStates.indexOf(sChildCurrent);
						// Check Target State index with object index location
						if("LT".equals(sComparisonOperator))
						{
							if(index >= indexTargetState)
							{
								arguments = new String[13];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.EqualOrAfter";
								arguments[1] = "5";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "State";
								arguments[9] = sChildCurrent;
								arguments[10] = "TargetState";
								arguments[11] = sTargetState;
								arguments[12] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
						}
						else if("GT".equals(sComparisonOperator))
						{
							if(index <= indexTargetState)
							{
								arguments = new String[13];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.EqualOrBefore";
								arguments[1] = "5";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "State";
								arguments[9] = sChildCurrent;
								arguments[10] = "TargetState";
								arguments[11] = sTargetState;
								arguments[12] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
						}
						else if("EQ".equals(sComparisonOperator))
						{
							if(index != indexTargetState)
							{
								arguments = new String[11];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.NotIn";
								arguments[1] = "4";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "State";
								arguments[9] = sTargetState;
								arguments[10] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
						}
						else if("LE".equals(sComparisonOperator))
						{
							if(index > indexTargetState)
							{
								arguments = new String[13];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.After";
								arguments[1] = "5";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "State";
								arguments[9] = sChildCurrent;
								arguments[10] = "TargetState";
								arguments[11] = sTargetState;
								arguments[12] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
						}
						else if("GE".equals(sComparisonOperator))
						{
					    	//dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - Start						
							if((index < indexTargetState) && (isLastRevision && indexChildState < strListChildStates.size()-2))
					    	//dso2013.5 - ALM:3586 - [Lifecycle and CM]State issues promoting an experimental bom to pilot or production - End							
							{
								arguments = new String[13];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.Before";
								arguments[1] = "5";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "State";
								arguments[9] = sChildCurrent;
								arguments[10] = "TargetState";
								arguments[11] = sTargetState;
								arguments[12] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
						}
						else if("NE".equals(sComparisonOperator))
						{
							if(index == indexTargetState)
							{
								arguments = new String[11];
								arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.Equal";
								arguments[1] = "4";
								arguments[2] = "Type";
								arguments[3] = sChildType;
								arguments[4] = "Name";
								arguments[5] = sChildName;
								arguments[6] = "Rev";
								arguments[7] = sChildRev;
								arguments[8] = "State";
								arguments[9] = sChildCurrent;
								arguments[10] = "";
								intOutput = 1;
								strOutput = strOutput + mailUtil.getMessage(context,arguments);
							}
						}
						else
						{
							arguments = new String[5];
							arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.InvalidOperator";
							arguments[1] = "1";
							arguments[2] = "Operation";
							arguments[3] = sComparisonOperator;
							arguments[4] = "";
							intOutput = 1;
							strOutput = strOutput + mailUtil.getMessage(context,arguments);
							break;
						}
					}
					
                  }
              }
              else if("required".equalsIgnoreCase(sObjectRequired))
              {
                  arguments = new String[7];
                  arguments[0] = "emxFramework.ProgramObject.eServicecommonCheckRelState.NoObject";
                  arguments[1] = "2";
                  arguments[2] = "Rel";
                  arguments[3] = sRel;
                  arguments[4] = "Object";
                  arguments[5] = sTargObject;
                  arguments[6] = "";
                  intOutput = 1;
                  strOutput = mailUtil.getMessage(context,arguments);
              }
          }
          if(intOutput != 0)
          {
        	//DSM (DS) 2015x.4 - Implementing CA Promotion through Background Job - START
		// ${CLASS:emxContextUtil}.mqlNotice(context,strOutput);
                throw(new Exception(strOutput));
                //DSM (DS) 2015x.4 - Implementing CA Promotion through Background Job - END
          }
          return intOutput;
      }
	  //DSO 2013x.4 , added for RalatinoShip Matrix-Rev3 Stages, When the Parent is in the same Stage as the Children Transport Unit the child must be in a higher/same lifecycle State as that of the Parent. -End
	   public int ensureDesignResponsibilityExists(Context context, String[] args)
      throws Exception
    {
       DebugUtil.debug("in ensureDesignResponsibilityExists()");
        String sOriginatingSource = getInfo(context,"attribute[pgOriginatingSource].value");
        if(UIUtil.isNotNullAndNotEmpty(sOriginatingSource) && sOriginatingSource.equalsIgnoreCase("Enginuity")) {
            return 0;
        }
        //Ensure Design Responsibility Exists
        if (!hasRelatedObjects(context,RELATIONSHIP_DESIGN_RESPONSIBILITY,false))
        {
          String strMessage = EngineeringUtil.i18nStringNow("emxEngineeringCentral.CheckDesignResponsibilitySetForObject.Message",context.getSession().getLanguage());
          ${CLASS:emxContextUtil}.mqlNotice(context,strMessage);
          return 1;
        }
        return 0;
    }
	
		public Vector getTitleForSubstitute(Context context,String[] args)throws Exception
		{
			Vector columnVals = new Vector();
			String sPartTitle = null;
			String sEBOMSubsRelid  = null;
			String strAttributeTitle = PropertyUtil.getSchemaProperty("attribute_Title");
			SelectList resultSelects    = new SelectList(3);
			resultSelects.add("to.attribute["+strAttributeTitle+"]");
	    		//DSM (DS) 2015x.5 - ALM 15293 - the title of substitue are not displayed correctly when have No Access - START 
			boolean isContextPushed = false;
			//DSM (DS) 2015x.5 - ALM 15293 - the title of substitue are not displayed correctly when have No Access - END 
	    	try{
			//DSM (DS) 2015x.5 - ALM 15293 - the title of substitue are not displayed correctly when have No Access - START 
			ContextUtil.pushContext(context);
			isContextPushed = true;	
			//DSM (DS) 2015x.5 - ALM 15293 - the title of substitue are not displayed correctly when have No Access - END 
				
					HashMap programMap                = (HashMap)JPO.unpackArgs(args);
					MapList objList                   = (MapList)programMap.get("objectList");
					Iterator i                        = objList.iterator();
					while (i.hasNext())
					{
						Map m                        = (Map) i.next();
						sEBOMSubsRelid       		 = (String)m.get("EBOM ID");
						String[] RelIdArray          = new String[1];
						RelIdArray[0]                = sEBOMSubsRelid;
						MapList resultList           = DomainRelationship.getInfo(context,RelIdArray,resultSelects);
						Iterator itr                 = resultList.iterator();
						Map map = (Map) itr.next();
						sPartTitle              =(String)map.get("to.attribute["+strAttributeTitle+"]");
						columnVals.add(sPartTitle);
				}	
			}
			catch(Exception ex)
	    	{
	    		//DSM (DS) 2015x.5 - ALM 15293 - the title of substitue are not displayed correctly when have No Access - START 
	    	}finally{
				if(isContextPushed){
					ContextUtil.popContext(context);
				}
			}
			//DSM (DS) 2015x.5 - ALM 15293 - the title of substitue are not displayed correctly when have No Access - END
	    	return columnVals;
		}	
		
		
		public MapList getSubstitutePart(Context context, String[] args)
        throws Exception
   {
    try
   {
            MapList _substitutePartList = null;
        MapList _finalsubstitutePartList        = new MapList();
//        MapList _finalsubstitutePartList1       = new MapList();
       HashMap paramMap = (HashMap)JPO.unpackArgs(args);
//       MapList objectList = (MapList)paramMap.get("objectList");
//       Map paramList = (HashMap)paramMap.get("paramList");
       String partId= (String) paramMap.get("objectId");
       String sEBOMSubRel = PropertyUtil.getSchemaProperty("relationship_EBOMSubstitute");
       //DSO 2013x.4 Added changes to include Valid Start and Valid Until Date : START
       String strParentId = ${CLASS:pgDSOCommonUtils}.INSTANCE.getParam(args, "parentId");
       String strFromEBOM = ${CLASS:pgDSOCommonUtils}.INSTANCE.getParam(args, "fromEBOM");
       if(UIUtil.isNotNullAndNotEmpty(strParentId) && "true".equalsIgnoreCase(strFromEBOM))
       {
    	   partId = strParentId;
       }
       //DSO 2013x.4 Added changes to include Valid Start and Valid Until Date : END
        //Apollo (DS) 2018x.1 - Substitute Report - starts
		boolean isLPD = ${CLASS:pgDSMLayeredProductUtil}.isLayeredProductPart(context,partId,null);		
		//Apollo (DS) 2018x.1 - Substitute Report - ends	   
        DomainObject domObj                     = new DomainObject(partId);
        StringList slEBOMSubs                   = null;
        StringList relsel                       = new StringList();
        String srealAttrFNName                  = PropertyUtil.getSchemaProperty(context,"attribute_FindNumber");
        String srealAttrQtyName                 = PropertyUtil.getSchemaProperty(context,"attribute_Quantity");
        //String RELATIONSHIP_EBOM_SUBSTITUTE     = PropertyUtil.getSchemaProperty(context,"relationship_EBOMSubstitute");
        relsel.add(DomainRelationship.SELECT_ID);
        relsel.add("attribute[" + srealAttrFNName + "]");
        relsel.add("attribute[" + srealAttrQtyName + "]");
        //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - starts
        relsel.add("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id");
        //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - ends
        StringList strObjList                   = new StringList(6);
        strObjList.add(DomainConstants.SELECT_ID);
        strObjList.add(DomainConstants.SELECT_TYPE);
        strObjList.add(DomainConstants.SELECT_NAME);
        strObjList.add(DomainConstants.SELECT_REVISION);
        _substitutePartList = domObj.getRelatedObjects(context,
                                        DomainConstants.RELATIONSHIP_EBOM,
                                        TYPE_PART,
                                        strObjList,
                                        relsel,
                                        false,
                                        true,
                                        (short)1,
                                        "",
                                        "");
        Iterator itr1 = _substitutePartList.iterator();
        Map tempmap1 = null;
        StringList sIDList = new StringList();
        while (itr1.hasNext())
                {
            Map relmap                  = (Map) itr1.next();
            String sEBOMID              = (String)relmap.get("id[connection]");
//            String sId                  = (String)relmap.get("id");
            String strCommand           = "print connection $1 select $2 dump $3";
			//DSM (DS) 2015x.4 ALM 14049 - Performance Issue - starts
            //String strMessage           = MqlUtil.mqlCommand(context,strCommand,sEBOMID,"frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id","|");
			slEBOMSubs = new StringList();
			if(relmap.containsKey("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id")) {
				Object ebomsubsObj =  relmap.get("frommid["+RELATIONSHIP_EBOM_SUBSTITUTE+"].id");
				if(ebomsubsObj instanceof String)
					slEBOMSubs.add((String)ebomsubsObj);
				else if(ebomsubsObj instanceof StringList)
					slEBOMSubs = (StringList)ebomsubsObj;
			}
			// slEBOMSubs                  = FrameworkUtil.split(strMessage,"|");
			//DSM (DS) 2015x.4 ALM 14049 - Performance Issue - ends
            Iterator ebomsubsItr        =slEBOMSubs.iterator();
            String sEBOMSubstituteRelid ="";
            while(ebomsubsItr.hasNext())
                        {
            	StringList slSubstituteForList = new StringList();
            	StringList slSubstituteForParentList = new StringList();
                tempmap1                        = new HashMap();
                tempmap1.put("EBOM ID",sEBOMID);
                //EBOM Substitute Id
                sEBOMSubstituteRelid            = (String) ebomsubsItr.next();
                //Putting EBOM Substitute Rel Id as id[connection]
                tempmap1.put("id[connection]",sEBOMSubstituteRelid);
              //DSM (DS) 2015x.4 REQT 14696 - Product Data Part Templates -starts
                tempmap1.put(DomainObject.SELECT_TYPE,relmap.get(DomainObject.SELECT_TYPE));
              //DSM (DS) 2015x.4 REQT 14696 - Product Data Part Templates -ends
                DomainRelationship domRel       = new DomainRelationship(sEBOMSubstituteRelid);
                //Getting relationship attribute on EBOM Substitute
                //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - starts
                /*Map attEbomSubstitute           = domRel.getAttributeMap(context,sEBOMSubstituteRelid);
                String strFindNumber            = (String) attEbomSubstitute.get(srealAttrFNName);
                String strQantity = (String) attEbomSubstitute.get(srealAttrQtyName); */
                //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - ends
                SelectList resultSelects        = new SelectList(1);
                String[] RelIdArray             = new String[1];
                RelIdArray[0]                   = sEBOMSubstituteRelid;
                resultSelects.add("to."+DomainObject.SELECT_ID);
                //DSM (DS) 2018x.0 ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - starts
                resultSelects.add("to."+DomainObject.SELECT_TYPE);
              //DSM (DS) 2018x.0 ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - ends
                //TSH nov 7 - begin
                resultSelects.add("fromrel.to."+DomainObject.SELECT_ID);
                resultSelects.add("fromrel.from."+DomainObject.SELECT_ID);
                //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - starts
                resultSelects.add("attribute[" + srealAttrFNName + "]");
                resultSelects.add("attribute[" + srealAttrQtyName + "]");
				
				//Apollo (DS) 2018x.1 - Substitute Report - starts
				if(isLPD)
				{
				resultSelects.add(DomainRelationship.SELECT_ID);
				resultSelects.add("attribute[pgNetWeight]");
				resultSelects.add("attribute[pgGrossWeightReal]");
				resultSelects.add("to.attribute[pgBaseUnitOfMeasure]");
				resultSelects.add("fromrel.to."+DomainObject.SELECT_NAME);
				resultSelects.add("to."+DomainObject.SELECT_NAME);
				}
				//Apollo (DS) 2018x.1 - Substitute Report - ends
                //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - ends
                //TSH nov 7 - end
                MapList resultList              = DomainRelationship.getInfo(context,
                                                           RelIdArray,
                                                           resultSelects);
                Iterator itr                    = resultList.iterator();
                Map map = (Map) itr.next();
                String strsubsPartId            = (String)map.get("to.id");
                tempmap1.put("id",strsubsPartId);
              //DSM (DS) 2018x.0 ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - starts
                tempmap1.put("SubstituteType",(String)map.get("to."+DomainObject.SELECT_TYPE));
              //DSM (DS) 2018x.0 ALM 23952 - Incorrect "Specification Sub Type" is getting display in Substitute tab - ends
                //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - starts
                tempmap1.put("attribute[" + srealAttrFNName + "]",(String)map.get("attribute[" + srealAttrFNName + "]"));
                tempmap1.put("attribute[" + srealAttrQtyName + "]",(String)map.get("attribute[" + srealAttrQtyName + "]"));
				
				//Apollo (DS) 2018x.1 - Substitute Report - starts
				if(isLPD)
				{
				tempmap1.put("attribute[pgNetWeight]",(String)map.get("attribute[pgNetWeight]"));
				tempmap1.put("attribute[pgGrossWeightReal]",(String)map.get("attribute[pgGrossWeightReal]"));
				tempmap1.put("Substitute Part",(String)map.get("to."+DomainObject.SELECT_NAME));
				tempmap1.put("BaseUOM",(String)map.get("to.attribute[pgBaseUnitOfMeasure]"));
				tempmap1.put("name",(String)map.get("fromrel.to."+DomainObject.SELECT_NAME));
				}
				//Apollo (DS) 2018x.1 - Substitute Report - ends
                //DSM (DS) 2015x.4 ALM 14049 - Performance Issue - ends
                //TSH - Begin
                DomainObject subsObj = new DomainObject(strsubsPartId);
                String subsType = (String) subsObj.getInfo(context, CPNCommonConstants.SELECT_TYPE);
                if (!subsType.equals((String)PropertyUtil.getSchemaProperty(context, "type_ParentSub")))
                {
                	 _finalsubstitutePartList.add((Map)tempmap1);
                }
                else
        		{
                	//slSubstituteForList.add((String)map.get("fromrel.to.id"));
                	 if(sIDList.contains(strsubsPartId))
                     {
                     	break;
                     }
                	 slSubstituteForParentList.add((String)map.get("fromrel.from.id"));
                	 sIDList.add(strsubsPartId);
        			 StringList objectSelects = new StringList();
        			 objectSelects.add(CPNCommonConstants.SELECT_NAME);
        			 objectSelects.add(CPNCommonConstants.SELECT_ID);
        			 objectSelects.add(CPNCommonConstants.SELECT_TYPE);
        			 objectSelects.add(CPNCommonConstants.SELECT_REVISION);
        			 StringList relSelects = new StringList();
        			 relSelects.add("attribute[Quantity].value");
        			 MapList SubInEBOM = subsObj.getRelatedObjects(context,
        		        CPNCommonConstants.RELATIONSHIP_EBOM,
        		        "*",
        		        objectSelects,
        		        relSelects,
        		        false,
        		        true,
        		        (short) 1,
        		        null,
        		        null);
        			 tempmap1.put("SubInEBOM",SubInEBOM);
        			 MapList subEBOM = null;
     				String strCmd           = "print bus $1 select $2 dump $3";
     			    String strMessage1           = MqlUtil.mqlCommand(context,strCmd,strsubsPartId,"to["+sEBOMSubRel+"].id","|");
     			    StringList slEBOMSubs1       = FrameworkUtil.split(strMessage1,"|");
     			    Iterator ebomsubsItr1        = slEBOMSubs1.iterator();
     			    String subRelId = null;
     			    String subId = null;
     			    while(ebomsubsItr1.hasNext())
     			    {
     			    	subRelId			 			= (String) ebomsubsItr1.next();
     			    	String strCmd1  		 	= "print connection $1 select $2 dump $3";
         			    String subForParentId      	= MqlUtil.mqlCommand(context,strCmd1,subRelId,"fromrel.to.id","|");
         			    if(!slSubstituteForList.contains(subForParentId))
         			    {
         			   slSubstituteForList.add(subForParentId);
         			    }
     			    }
        			 tempmap1.put("SubForParts",slSubstituteForList);
                     tempmap1.put("SubstituteForParentList",slSubstituteForParentList);
         			 _finalsubstitutePartList.add((Map)tempmap1);
                }
                //TSH - End
                        }
                    }
         ContextUtil.startTransaction(context, false);
         ContextUtil.commitTransaction(context);
         return _finalsubstitutePartList;
     }// end of try
     catch (Exception e)
         {
      ContextUtil.abortTransaction(context);
      throw new FrameworkException(e);
         }
	}	

		  public MapList getEBOMsWithRelSelectablesSB(Context context, String[] args) throws Exception{

				 HashMap paramMap = (HashMap) JPO.unpackArgs(args);



				 String sExpandLevels = getStringValue(paramMap, "emxExpandFilter");

				 String selectedFilterValue = getStringValue(paramMap, "ENCBOMRevisionCustomFilter");

				 MapList retList = expandEBOM(context, paramMap);
				 Map obj1 = null;
				 MapList ebomHiRList = new MapList();
				 String strValue = "";
				 MapList finalRetList = new MapList();
				//DSM (DS) ALM 9608: Bill of Material on APP, DPP, and FOP is visible to CMs who are Authorized to Use - START
				 String contextId = getStringValue(paramMap, "parentOID");
				 String parentBOMcontextType = DomainObject.newInstance(context, contextId).getInfo(context,DomainConstants.SELECT_TYPE);
				 //StringList packagingTypeList = createStringList(new String[] {${CLASS:pgDSOConstants}.TYPE_FINISHEDPRODUCT_PART, ${CLASS:pgDSOConstants}.TYPE_PG_CUSTOMERUNIT,${CLASS:pgDSOConstants}.TYPE_PG_CONSUMERUNIT, ${CLASS:pgDSOConstants}.TYPE_PACKAGINGASSEMBLYPART});
				 //boolean isctxtPackagingPart = packagingTypeList.contains(contextType);
				 StringList noExpandList = createStringList(new String[] {${CLASS:pgDSOConstants}.TYPE_ASSEMBLED_PRODUCT_PART, ${CLASS:pgDSOConstants}.TYPE_DEVICE_PRODUCT_PART,${CLASS:pgDSOConstants}.TYPE_FORMULATED_PRODUCT, ${CLASS:pgDSOConstants}.TYPE_FORMULATION_PART});
				 String relId = DomainConstants.EMPTY_STRING;
				 
				 String strTOType = DomainConstants.EMPTY_STRING;
				 String strFromId = DomainConstants.EMPTY_STRING;
				 String strFromType = DomainConstants.EMPTY_STRING;
				 StringList fromExpandList = createStringList(new String[] {"from.type", "from.id"});
				 //DSM2015x.1 - HiRFormula Card- Expand issue - START
				 //Iterate through the expanded list to get the formula card in the BOM
				 for(int index=0; index < retList.size(); index++){
					obj1 = (Map)retList.get(index);
					strValue = (String)obj1.get("from[EBOM]");
					strTOType = (String)obj1.get(DomainConstants.SELECT_TYPE);
					relId = (String)obj1.get("id[connection]");
					//DSM(DS) 2018x.0 ALM 22129 - Performance improvement for CSS BOM page loading - STARTS
					//Removed extra DB calls to query BOM info and instead add selectables in expandEBOM API itself
					/*if(UIUtil.isNotNullAndNotEmpty(relId)) {
						MapList relmapList = DomainRelationship.getInfo(context, new String[] {relId}, fromExpandList);
						strFromType = (String) ((Map)(relmapList.get(0))).get("from.type");
						strFromId = (String) ((Map)(relmapList.get(0))).get("from.id");
					}*/
					strFromType = (String)obj1.get("from.type");
					strFromId = (String)obj1.get("from.id");
					//DSM(DS) 2018x.0 ALM 22129 - Performance improvement for CSS BOM page loading - ENDS
					if("#DENIED!".equals(strValue) || ((!noExpandList.contains(parentBOMcontextType)) && (noExpandList.contains(strTOType) || noExpandList.contains(strFromType)))){
						Part partObj = new Part((String)obj1.get(SELECT_ID));
						//DSM (DS) ALM 10290 : Revise from IPM FC to DPP drops the BOM - START
						if((!"#DENIED!".equals(strValue)) && (!(noExpandList.contains(strTOType)) && noExpandList.contains(strFromType)))
						{
						//DSM (DS) ALM 10290 : Revise from IPM FC to DPP drops the BOM - END
							partObj.setId(strFromId) ;
						}
						//DSM (DS) ALM 9608: Bill of Material on APP, DPP, and FOP is visible to CMs who are Authorized to Use - END
						StringList objectSelect = createStringList(new String[] {SELECT_ID,DomainConstants.SELECT_TYPE,DomainConstants.SELECT_NAME,DomainConstants.SELECT_REVISION});

						StringList relSelect = createStringList(new String[] {DomainConstants.SELECT_RELATIONSHIP_ID,SELECT_ATTRIBUTE_FIND_NUMBER});

						//fetch the BOM under HiR Formula card
						ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
						ebomHiRList = partObj.getRelatedObjects(context,

								          							 RELATIONSHIP_EBOM,

								          							 TYPE_PART,

								          							 objectSelect,

								          							 relSelect,

									                                 false,

									                                 true,

									                                 (short)0,

									                                 null, null, 0);
						ContextUtil.popContext(context);
						for(int i=0;i<ebomHiRList.size();i++){
							finalRetList.add((Map)ebomHiRList.get(i));
						}
						//break;				
						// Need not break as BOM might have more than one FCs instead add it to finalRetList and remove such BOM items at once
					}
				 }
				 //Loop to iterate and remove the HiR Formula card BOM from the expanded List.
				 for(int i=0;i<finalRetList.size();i++){
					Map obj2 = (Map)finalRetList.get(i);
					//DSM (DS) ALM 9608: Modified bus id to rel id - Start
					String objectId = (String)obj2.get(DomainConstants.SELECT_RELATIONSHIP_ID);
					//DSM (DS) ALM 9608: Modified bus id to rel id - END
					for(int j=0;j<retList.size();j++){
						Map obj3 = (Map)retList.get(j);
						//DSM (DS) ALM 9608: Modified bus id to rel id - Start
						if(objectId.equals(obj3.get(DomainConstants.SELECT_RELATIONSHIP_ID))){
							//DSM (DS) ALM 9608: Modified bus id to rel id - End
							retList.remove(j);
							break;
						}
					}
				 }
				//DSM2015x.1 - HiRFormula Card- Expand issue - END
				 if("Latest".equals(selectedFilterValue) || "Latest Complete".equals(selectedFilterValue)){

					// handles manual expansion by each level for latest and latest complete

					 int expandLevel = "All".equals(sExpandLevels)? 0: Integer.parseInt(sExpandLevels);

					 MapList childList = null;

					 Map obj = null;

					 int level;

					 for(int index=0; index < retList.size(); index++){

						 obj = (Map)retList.get(index);

						 if(expandLevel == 0 || Integer.parseInt((String)obj.get("level")) < expandLevel){
							 
							 paramMap.put("partId", (String)obj.get(SELECT_ID));

							 childList = expandEBOM(context, paramMap);

							 if(childList!=null && !childList.isEmpty()){

								 for(int cnt=0; cnt<childList.size(); cnt++){

									 level = Integer.parseInt((String)obj.get("level"))+1;

									((Map)childList.get(cnt)).put("level", String.valueOf(level));

								 }

								 retList.addAll(index+1,childList);

							 }

						}

					 }

				 }

				 return retList;

			 }
	//DSM(DS) 2015x.1 Code to display title stage and BUOM on HiR FC part START
		/**
	 * DSO 2015x.1 - This method is used to show the Title column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return Title values
	 */	 
	public Vector getTitle(Context context,String args[]) throws Exception

     {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList mlObjectList = (MapList) programMap.get("objectList");
     	    Vector fnValuesVector = new Vector(mlObjectList.size());
			String strTitle;
			String[] arrayId = new String[mlObjectList.size()];
			if(!mlObjectList.isEmpty())
			{
				Map mpObjectMap = new HashMap();
				for(int i=0;i< mlObjectList.size();i++)
				{
					mpObjectMap = (Map)mlObjectList.get(i);
					arrayId[i] = (String)mpObjectMap.get(DomainObject.SELECT_ID);
				}
			}
			strTitle = pgV3Constants.SELECT_ATTRIBUTE_TITLE;
			fnValuesVector = getValues(context,arrayId,strTitle);
			return fnValuesVector;
	 }
	 
		/**
	 * DSO 2015x.1 - This method is used to show the Stage column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return Stage values
	 */	
	 	public Vector getStage(Context context,String args[]) throws Exception

     {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
		
			MapList mlObjectList = (MapList) programMap.get("objectList");
     	    Vector fnValuesVector = new Vector(mlObjectList.size());
			String strStage;
			String[] arrayId = new String[mlObjectList.size()];
			if(!mlObjectList.isEmpty())
			{
				Map mpObjectMap = new HashMap();
				for(int i=0;i< mlObjectList.size();i++)
				{
					mpObjectMap = (Map)mlObjectList.get(i);
					arrayId[i] = (String)mpObjectMap.get(DomainObject.SELECT_ID);
				}
			}
			//Added for PnG Upgrade 2018x Starts - Modified the attribute name as attribute to store the stage value is changed to Release phase in 18x OOTB.
			String sRelPhase = PropertyUtil.getSchemaProperty(context, "attribute_ReleasePhase");
			strStage = "attribute["+sRelPhase+"]";
			//Added for PnG Upgrade 2018x Ends
			fnValuesVector = getValues(context,arrayId,strStage.toString());
			return fnValuesVector;
	 }
	 
	 /**
	 * DSO 2015x.1 - This method is used to show the UOM column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return UOM values
	 */	
	 //Modified return type to StringList for 2018x Upgrade since   method with same name added to emxECPartBase with return type Vector and will be considered as Overidden method.
	 public StringList getUOM(Context context,String args[]) throws Exception
	//public Vector getUOM(Context context,String args[]) throws Exception
     {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList mlObjectList = (MapList) programMap.get("objectList");
     	    Vector fnValuesVector = new Vector(mlObjectList.size());
			String strUOM;
			String[] arrayId = new String[mlObjectList.size()];
			if(!mlObjectList.isEmpty())
			{
				Map mpObjectMap = new HashMap();
				for(int i=0;i< mlObjectList.size();i++)
				{
					mpObjectMap = (Map)mlObjectList.get(i);
					arrayId[i] = (String)mpObjectMap.get(DomainObject.SELECT_ID);
				}
			}
			strUOM = "attribute[pgBaseUnitOfMeasure]";
			fnValuesVector = getValues(context,arrayId,strUOM.toString());
			//Added for 2018x Upgrade Starts
			StringList resultList = new StringList();
			resultList.addAll(fnValuesVector);
			return resultList;
			//Added for 2018x Upgrade Ends

	 }
	 /**
	 * DSM(DS) 2015x.2 - This method is used to show the OnShelf Product Density column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return OnShelf Product Density values
	 */	
	 public Vector getpgOnshelfProductDensity(Context context,String args[]) throws Exception

     {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList mlObjectList = (MapList) programMap.get("objectList");
     	    Vector fnValuesVector = new Vector(mlObjectList.size());
			String strOnShelfDensity;
			String[] arrayId = new String[mlObjectList.size()];
			if(!mlObjectList.isEmpty())
			{
				Map mpObjectMap = new HashMap();
				for(int i=0;i< mlObjectList.size();i++)
				{
					mpObjectMap = (Map)mlObjectList.get(i);
					arrayId[i] = (String)mpObjectMap.get(DomainObject.SELECT_ID);
				}
			}
			strOnShelfDensity = "attribute[pgOnshelfProductDensity]";
			fnValuesVector = getValues(context,arrayId,strOnShelfDensity.toString());
			return fnValuesVector;
	 }
	 /**
	 * DSM(DS) 2015x.2 - This method is used to show the State column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return State or Current
	 */	
	 public Vector getState(Context context,String args[]) throws Exception

     {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList mlObjectList = (MapList) programMap.get("objectList");
     	    Vector fnValuesVector = new Vector(mlObjectList.size());
			String strState;
			String[] arrayId = new String[mlObjectList.size()];
			if(!mlObjectList.isEmpty())
			{
				Map mpObjectMap = new HashMap();
				for(int i=0;i< mlObjectList.size();i++)
				{
					mpObjectMap = (Map)mlObjectList.get(i);
					arrayId[i] = (String)mpObjectMap.get(DomainObject.SELECT_ID);
				}
			}
			strState = DomainConstants.SELECT_CURRENT;
			fnValuesVector = getValues(context,arrayId,strState.toString());
			return fnValuesVector;
	 }
	 /**
	 * DSM(DS) 2015x.2 - This method is used to show the Density UOM column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return Density Unit of Measure
	 */	
	 public Vector getDesityUoM(Context context,String args[]) throws Exception

     {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList mlObjectList = (MapList) programMap.get("objectList");
     	    Vector fnValuesVector = new Vector(mlObjectList.size());
			String strDensityUOM;
			String[] arrayId = new String[mlObjectList.size()];
			if(!mlObjectList.isEmpty())
			{
				Map mpObjectMap = new HashMap();
				for(int i=0;i< mlObjectList.size();i++)
				{
					mpObjectMap = (Map)mlObjectList.get(i);
					arrayId[i] = (String)mpObjectMap.get(DomainObject.SELECT_ID);
				}
			}
			strDensityUOM = "attribute[pgDensityUOM]";
			fnValuesVector = getValues(context,arrayId,strDensityUOM.toString());
			return fnValuesVector;
	 }
	 
	 /**
	 * DSO 2015x.1 - This method is used to get the column value on PDP BOM page.
	 * @param context
	 * @param args
	 * @return respective values
	 */	
	 public Vector getValues(Context context,String[] arrayId,String strSelect) throws Exception

     {
		Vector fnValuesVector = new Vector(arrayId.length);
		String strValue;
		StringList slSelect = new StringList(1);
		slSelect.addElement(strSelect);
		if(arrayId != null && arrayId.length>0)
				{
					ContextUtil.pushContext(context);
					MapList mlObjInfo = DomainObject.getInfo(context,arrayId,slSelect);
					ContextUtil.popContext(context);
					Map map = new HashMap();
					if(!mlObjInfo.isEmpty() && mlObjInfo != null)
					{
						for(int j=0;j<mlObjInfo.size();j++)
						{
							map = (Map)mlObjInfo.get(j);
							if(!map.isEmpty() && map != null)
							{
								strValue = (String)map.get(strSelect);
								fnValuesVector.addElement(strValue);
							}
						}
					}
				}
				
			return fnValuesVector;
	}
	//DSM(DS) 2015x.1 Code to display title stage and BUOM on HiR FC part END

	//DSM (DS) 2015X.1 : overridden method for revise MEP : START
	 /**

	  * Clears the Start and End Effectivity dates on the EBOM or Manufacturer Equivalent connection

	  * between this assembly and any child components.

	  *

	  * @param context the eMatrix <code>Context</code> object.

	  * @param args holds the following input arguments:

	  *        0 - the symbolic name of the relationship to float, either

	  *            relationship_ManufacturerEquivalent or

	  *            relationship_EBOM (default if none specified)

	  * @throws Exception if the operation fails.

	  * @since EC 9.5.JCI.0.

	  * @trigger TypePartReviseAction.

	  */

	 public void clearEBOMEffectivity(Context context, String[] args) throws Exception {

		 DebugUtil.debug("TypePartReviseAction:clearEBOMEffectivity");
		 boolean isContextPushed = false;

		 try
		 {

			 // This method is now also used to clear the Manufacturer Equivalent Rel attributes
			 // First get the symbolic name of the relationship to expand on

			 String rel_SymbolicName = args[0];
			 String select = null;

			 // If there is no relationship name passed in then default to EBOM
			 if ("relationship_ManufacturerEquivalent".equals(rel_SymbolicName))
			 {
				 select = "next.from[" + DomainObject.RELATIONSHIP_MANUFACTURER_EQUIVALENT + "].id";
			 }
			 else
			 {
				 select = "next.from[" + DomainObject.RELATIONSHIP_EBOM + "].id";
			 }
			 StringList sList = getInfoList(context, select);
			 Iterator i = sList.iterator();
			 HashMap attributes = new HashMap();
			 attributes.put(DomainConstants.ATTRIBUTE_START_EFFECTIVITY_DATE, "");
			 attributes.put(DomainConstants.ATTRIBUTE_END_EFFECTIVITY_DATE, "");

			 String relId = null;

			 ContextUtil.pushContext(context, PropertyUtil.getSchemaProperty(context, "person_UserAgent"), DomainConstants.EMPTY_STRING, DomainConstants.EMPTY_STRING);
			 isContextPushed = true;

			 while (i.hasNext())
			 {
				 relId = (String)i.next();
				 // Set start and end effectivity to blank for this new revision of
				 // assembly part
				 DomainRelationship.setAttributeValues(context, relId, attributes);
			 }
		 }
		 catch (Exception ex)
		 {
			 throw ex;
		 }finally{
			 if(isContextPushed){
				 ContextUtil.popContext(context);
			 }
		 }
	 }
	//DSM (DS) 2015X.1 : overridden method for revise MEP : END
	 
	 //DSM (DS) 2015X.1 : overridden method for TUP BOM Issue : Start	 
	 /**

	     * Disconnect/Connect EBOM relationship.

	     * @param context the eMatrix <code>Context</code> object.

	     * @param args holds a packed HashMap.

	     * @return connection/disconnection result.

	     * @throws Exception if the operation fails.

	     */

	    @com.matrixone.apps.framework.ui.ConnectionProgramCallable

	    public HashMap updateBOM (Context context, String args[]) throws Exception

	    {

	    	MapList mlItems = new MapList();

	    	HashMap doc     = new HashMap();

			//DSM (DS) 2015x.5 ALM 15473 - The Chg Column is not editable on the BOM. Further more it matches any values put in the Change Column with no validation - START
			String strChgValue = DomainConstants.EMPTY_STRING;
			//DSM (DS) 2015x.5 ALM 15473 - The Chg Column is not editable on the BOM. Further more it matches any values put in the Change Column with no validation - END


	    	HashMap request        = (HashMap)JPO.unpackArgs(args);

	    	Element elm            = (Element)request.get("contextData");

	    	String strObjectId     = (String) request.get("objectId");



	    	java.util.List listObjects = elm.getChildren("object");

	    	Iterator itrObjects = listObjects.iterator();

	    	// 372458

	    	String connectAsDerived = EnoviaResourceBundle.getProperty(context, "emxEngineeringCentral.ReplaceBOM.Derived");

	    	String attrDerivedContext= PropertyUtil.getSchemaProperty(context,"attribute_DerivedContext");

	    	//Added for IR-021267

	    	HashMap hmpAttributes = new HashMap();

	    	Element eleObject     = null;

	    	String sRowId         = "";

	    	String strChildId     = "";

	    	String sRelId         = "";

	    	String sRelTypeSymb   = "";

	    	String markup         = "";

	    	String strParam2      = "";

	    	String sRelType = null;

	    	java.util.List listColumns= null;

	    	Iterator itrColumns = null;

	    	Element eleColumn = null;

	    	DomainObject parentObj = new DomainObject(strObjectId);



	    	DomainObject childObj  = new DomainObject();
	    	//DSM (DS) 2015X.1 : Added the code to fix issue - TUP BOM requires that the first item on the BOM must be a MCUP : Start
	    	String strErrorMessage = DomainConstants.EMPTY_STRING;

	    	try{

	    		ContextUtil.pushContext(context);	    	
	    		${CLASS:pgDSOCPNProductData} DSOProductData = new ${CLASS:pgDSOCPNProductData}(context, args);
	    		//DSM (DS) 2015x.4 - Implement BOM Rule Validation on save - START 
	    		//strErrorMessage = DSOProductData.hasMustHaveChildTypeObj(context, strObjectId, listObjects);
	    		//DSM (DS) 2015x.4 - Implement BOM Rule Validation on save - END
	    		//DSM (DS) 2015X.1 : Added the code to fix issue - TUP BOM requires that the first item on the BOM must be a MCUP : End
	    		while (itrObjects.hasNext()){

	    			//Modified for IR-021267

	    			hmpAttributes.clear();

	    			eleObject     = (Element) itrObjects.next();

	    			sRowId         = eleObject.getAttributeValue("rowId");

	    			strChildId     = eleObject.getAttributeValue("objectId");

	    			sRelId         = eleObject.getAttributeValue("relId");

	    			sRelTypeSymb   = eleObject.getAttributeValue("relType");

	    			markup         = eleObject.getAttributeValue("markup");

	    			strParam2      = eleObject.getAttributeValue("param2");



	    			sRelType = null;



	    			if (sRelTypeSymb != null){

	    				sRelType = (String) PropertyUtil.getSchemaProperty(context,sRelTypeSymb);

	    			}



	    			if ("add".equals(markup)){

	    				//Logic for ur ADD Opearation

	    				//get all attributes

	    				//Modified for IR-021267

	    				listColumns = eleObject.getChildren("column");

	    				itrColumns = listColumns.iterator();

	    				while (itrColumns.hasNext()){

	    					eleColumn = (Element) itrColumns.next();

	    					hmpAttributes.put(eleColumn.getAttributeValue("name"), eleColumn.getText());

	    				}

	                    //DSM(DS) 2015x.4 ALM 13801 - Setting Material Function values on Add - start  				
	    				String strMaterialFunction = (String) hmpAttributes.get(${CLASS:pgDSOConstants}.STR_MATERIAL_FUNCTION);
	    				hmpAttributes.remove(${CLASS:pgDSOConstants}.STR_MATERIAL_FUNCTION);	    				
	    				hmpAttributes.put(FormulationAttribute.APPLICATION.getAttribute(context), strMaterialFunction);
	                    //DSM(DS) 2015x.4 ALM 13801 - Setting Material Function values on Add - End
						//DSM (DS) 2015x.5 ALM 15473 - The Chg Column is not editable on the BOM. Further more it matches any values put in the Change Column with no validation - START
	    				strChgValue = (String) hmpAttributes.get(${CLASS:pgDSOConstants}.STR_CHG);
	    				hmpAttributes.remove(${CLASS:pgDSOConstants}.STR_CHG);	    				
	    				hmpAttributes.put(${CLASS:pgDSOConstants}.ATTR_PG_CHANGE, strChgValue);
	    				//DSM (DS) 2015x.5 ALM 15473 - The Chg Column is not editable on the BOM. Further more it matches any values put in the Change Column with no validation - END
	    				//Modified for 2018x Upgrade  Starts - Removed these attribute from map as these are Object attributes and it is not editable in UI 
	    				hmpAttributes.remove("UOM");
	    				hmpAttributes.remove("VPMVisible");
	    				//Modified for 2018x Upgrade  Ends 
						//DSM(DS) 2015x.5.1 - ALM#19831 - EBOMComments doesn't exit error message is displayed when user trying to add a new COP to an existing revised CUP - Start
						System.out.println("hmpAttributes --->"+hmpAttributes);
						hmpAttributes.put(${CLASS:pgDSOConstants}.ATTR_COMMENT,(String)hmpAttributes.get(${CLASS:pgDSOConstants}.CONST_EBOMCOMMENT));
						hmpAttributes.remove(${CLASS:pgDSOConstants}.CONST_EBOMCOMMENT);	
						System.out.println("hmpAttributes -after Fix-->"+hmpAttributes);
						//DSM(DS) 2015x.5.1 - ALM#19831 - EBOMComments doesn't exit error message is displayed when user trying to add a new COP to an existing revised CUP - End
	    				
	    				childObj.setId(strChildId);



	    				//connect the parent part and child part

	    				DomainRelationship dr = DomainRelationship.connect(context,

	    						parentObj,

	    						sRelType,

	    						childObj);



	    				//set attributes to EBOM realtionship

	    				dr.setAttributeValues(context, (Map) hmpAttributes);



	    				if (strParam2 != null && strParam2.length() > 0)

	    				{

	    					StringList slParamObjs = childObj.getInfoList(context, "from["+RELATIONSHIP_DERIVED+"]."+DomainConstants.SELECT_TO_ID);

	    					if(slParamObjs == null){

	    						slParamObjs = new StringList();

	    					}

	    					if(!slParamObjs.contains(strParam2)){

	    						// 372458

	    						if(connectAsDerived.equalsIgnoreCase("true")){

	    							/*DomainRelationship doRelDerived = DomainRelationship.connect(context,

	                                                                    childObj,

	                                                                    RELATIONSHIP_DERIVED,

	                                                                    new DomainObject(strParam2));*/

	    							DomainRelationship doRelDerived = DomainRelationship.connect(context,

	    									new DomainObject(strParam2),

	    									RELATIONSHIP_DERIVED,

	    									childObj);

	    							doRelDerived.setAttributeValue(context, attrDerivedContext, "Replace");

	    						}

	    					}

	    				}



	    				//creating a returnMap having all the details abt the changed row.

	    				HashMap returnMap = new HashMap();

	    				returnMap.put("oid", strChildId);

	    				returnMap.put("rowId", sRowId);

	    				returnMap.put("pid", parentObj.getId());

	    				returnMap.put("relid", dr.toString());

	    				returnMap.put("markup", markup);

	    				returnMap.put("columns", hmpAttributes);

	    				mlItems.add(returnMap);  //returnMap having all the details abt the changed row.



	    			}

	    			else if ("cut".equals(markup)){

	    				//	                    HashMap items=new HashMap();

	    				//Logic for ur CUT Opearation

	    				DomainRelationship.disconnect(context, sRelId);



	    				//creating a returnMap having all the details abt the changed row.

	    				HashMap returnMap = new HashMap();

	    				returnMap.put("oid", strChildId);

	    				returnMap.put("rowId", sRowId);

	    				returnMap.put("relid", sRelId);

	    				returnMap.put("markup", markup);

	    				returnMap.put("columns", hmpAttributes);

	    				mlItems.add(returnMap);



	    			}



	    			else if ("resequence".equals(markup)){

	    				//Logic for ur resequence Opearation



	    				//creating a returnMap having all the details abt the changed row.

	    				HashMap returnMap = new HashMap();

	    				returnMap.put("oid", strChildId);

	    				returnMap.put("rowId", sRowId);

	    				returnMap.put("relid", sRelId);

	    				returnMap.put("markup", markup);

	    				returnMap.put("columns", hmpAttributes);

	    				mlItems.add(returnMap);

	    			}

	    		}
	    		//DSM (DS) 2015X.1 : Added the code to fix issue - TUP BOM requires that the first item on the BOM must be a MCUP : Start
	    		if(UIUtil.isNullOrEmpty(strErrorMessage))
	    		{
	    			doc.put("Action", "success"); 

	    			doc.put("changedRows", mlItems);
	    		}
	    		else
	    		{
	    			doc.put("Action", "ERROR");

	    			doc.put("Message", strErrorMessage); 
	    		}
	    		//DSM (DS) 2015X.1 : Added the code to fix issue - TUP BOM requires that the first item on the BOM must be a MCUP : End

	    		ContextUtil.popContext(context);

	    	}

	    	catch(Exception e){

	    		doc.put("Action", "ERROR"); // If any exeception is there send "Action" as "ERROR"

	    		doc.put("Message", e.getMessage()); // Error message to Display

	    	}



	    	return doc;

	    }
	  //DSM (DS) 2015X.1 : overridden method for TUP BOM Issue : End

//DSM (DS) 2015X.1 : ALM #5958 : overridden method for Part Family Reference issue : Start	    
/**
 * overridden method for Part Family Reference issue from emxECPartBaseJPO
 * Establish Part Family Reference relationship between new rev part (Master/Reference Part) to old rev part (Master/Reference Part)
 * @param context the eMatrix <code>Context</code> object.
 * @param args holds the following input arguments:
 *        0 - Part object id that is being revised
 * @throws Exception if the operation fails.
 * @since X3.
 */

public void connectMasterPartsAndReferenceParts(Context context, String[] args)throws Exception {

	String strPartId = args[0];

	DomainObject dobj = new DomainObject(strPartId);

	DomainObject doPrevRevObjId = dobj,doLatestRev=dobj;

	String PartSeriesON = (String)dobj.getInfo(context, "interface["+ INTERFACE_PART_FAMILY_REFERENCE +"]");

	if(UIUtil.isNotNullAndNotEmpty(PartSeriesON) && "TRUE".equalsIgnoreCase(PartSeriesON)){	 

		BusinessObject previousRevObj=null;	        	

		String strPreviousRevPartId=null;

		BusinessObject lastRevObj=null;	        	

		String strLastRevPartId=null;

		String strState=null;

		String sContextPartFamilyId=null;

		StringList sNewRevClassifiedItemRelIdList=null;         	

		String sNewRevClassifiedItemRelId = null;   	

		String strResult = null; 	       		

		String strCommand=null; 		

		lastRevObj = dobj.getLastRevision(context);

		strLastRevPartId = lastRevObj.getObjectId(context);

		if(strLastRevPartId.equals(strPartId)){

			previousRevObj = doLatestRev.getPreviousRevision(context);

			strPreviousRevPartId = previousRevObj.getObjectId(context);

			doPrevRevObjId = new DomainObject(strPreviousRevPartId);

		}
		else{

			previousRevObj=(BusinessObject)dobj;

			doLatestRev = new DomainObject(strLastRevPartId);	    		

		}

		//for very 1st revision(master and ref) skip the execution as they don't have previous rev exist.

		if( previousRevObj != null && (!(previousRevObj.toString().trim().equals(".."))  ||  !(previousRevObj.getTypeName().equals("")))){

			String sRefType = (String)doPrevRevObjId.getInfo(context, "attribute["+ ATTRIBUTE_REFERENCE_TYPE +"]");

			strState= doLatestRev.getInfo(context, SELECT_CURRENT);

			if((SYMB_R.equals(sRefType) && (strState.equalsIgnoreCase(STATE_PART_PRELIMINARY)||strState.equalsIgnoreCase(STATE_DEVELOPMENT_PART_CREATE))) || (SYMB_M.equals(sRefType)&& (strState.equalsIgnoreCase(STATE_PART_RELEASE)||strState.equalsIgnoreCase(STATE_DEVELOPMENT_PART_COMPLETE)))){

				//DSM (DS) 2015X.1 : ALM #5958 : modified expression for Part Family Reference issue : Start
				sContextPartFamilyId = (String)doPrevRevObjId.getInfo(context, "relationship["+RELATIONSHIP_CLASSIFIED_ITEM+"].frommid["+RELATIONSHIP_PART_FAMILY_REFERENCE+"].torel.from.id");
				sNewRevClassifiedItemRelIdList = (StringList)doLatestRev.getInfoList(context, "relationship["+RELATIONSHIP_CLASSIFIED_ITEM+"|from.type==\""+TYPE_PART_FAMILY+"\"].id");
				//DSM (DS) 2015X.1 : ALM #5958 : modified expression for Part Family Reference issue : End	
				for(int i=0 ; i<sNewRevClassifiedItemRelIdList.size(); i++){

					sNewRevClassifiedItemRelId = (String)sNewRevClassifiedItemRelIdList.get(i);
					//DSM (DS) 2015X.1 : ALM #5958 : added for Part Family Reference issue : Start
					if(UIUtil.isNotNullAndNotEmpty(sNewRevClassifiedItemRelId) && sNewRevClassifiedItemRelId.indexOf("=") != -1){
						sNewRevClassifiedItemRelId = (FrameworkUtil.split(sNewRevClassifiedItemRelId , "=")).get(1).toString().trim();
					}
					//DSM (DS) 2015X.1 : ALM #5958 : added for Part Family Reference issue : End
					strResult = MqlUtil.mqlCommand(context,"print connection $1 select $2 dump",sNewRevClassifiedItemRelId,"from.id");

					if(strResult.equals(sContextPartFamilyId)){

						break;

					}

				}
				if(SYMB_R.equals(sRefType)){

					strCommand = "add connection \"$1\" fromrel $2 torel $3";

					StringList sMasterClassifiedItemRelId = (StringList)doPrevRevObjId.getInfoList(context, "relationship["+ RELATIONSHIP_CLASSIFIED_ITEM +"].frommid["+ RELATIONSHIP_PART_FAMILY_REFERENCE +"].torel.id");

					for(int i=0 ; i<sMasterClassifiedItemRelId.size(); i++){

						MqlUtil.mqlCommand(context,strCommand,RELATIONSHIP_PART_FAMILY_REFERENCE,sNewRevClassifiedItemRelId,(String)sMasterClassifiedItemRelId.get(i));

					}
				}

				if(SYMB_M.equals(sRefType)){

					strCommand = "modify connection $1 torel $2";

					StringList PART_FAMILY_REFERENCE_REL_ID = (StringList)doPrevRevObjId.getInfoList(context, "relationship["+ RELATIONSHIP_CLASSIFIED_ITEM +"].tomid["+ RELATIONSHIP_PART_FAMILY_REFERENCE +"].id");

					for(int i=0 ; i<PART_FAMILY_REFERENCE_REL_ID.size(); i++){ 

						MqlUtil.mqlCommand(context,strCommand,(String)PART_FAMILY_REFERENCE_REL_ID.get(i),sNewRevClassifiedItemRelId);

					}	    			

				}

			}

		}

	}

}
//DSM (DS) 2015X.1 : ALM #5958 : overridden method for Part Family Reference issue : End		

//Added by DSM Sogeti for BOM table exception - Starts
    public MapList getStoredEBOM(Context context, String[] args)

        throws Exception

    {

	//String sChildren = null;

     MapList ebomList = null;

     HashMap programMap = (HashMap)JPO.unpackArgs(args);

     String sMatchBasedOn = (String) programMap.get("matchBasedOn");

     try{

        ebomList = getEBOMsWithRelSelectablesSB (context, args);



        Iterator itr = ebomList.iterator();

        MapList tList = new MapList();

        StringList ebomDerivativeList = EngineeringUtil.getDerivativeRelationships(context, RELATIONSHIP_EBOM, true);

        while(itr.hasNext())

        {

            HashMap newMap = new HashMap((Map)itr.next());

            newMap.put("selection", "multiple");

			//Added for hasChildren starts

			//sChildren = (String)newMap.get("from["+DomainConstants.RELATIONSHIP_EBOM+"]");

			//newMap.put("hasChildren",sChildren);

			newMap.put("hasChildren",EngineeringUtil.getHasChildren(newMap, ebomDerivativeList));

			//Added for hasChildren ends

            tList.add (newMap);

        }


        ebomList.clear();

        ebomList.addAll(tList);

		//Added by DSM(Sogeti)2015x.2 To sort BOM structure based on Name for pgPhase and Find Number for childs for defect 7429 on 22-09-2016-Start
			MapList mlEBOMDataToSort = new MapList(10);
			Iterator itrEBOMData = ebomList.iterator();
			String strLastLevel = "1";
			MapList mlLastMapList= mlEBOMDataToSort;
			MapList mlReferenceList= new MapList(5);
			Map mPartEBOM = null;
			String strLevel=null;
			int intLastLevel=0;
			int intLevel=0;
			Map mLastMap =null;
			MapList mlChildMapList=null;
			MapList mlTemp=null;
			Map mReferenceMap =null;
			while(itrEBOMData.hasNext()) {
				mPartEBOM = (Map)itrEBOMData.next();
				strLevel = (String)mPartEBOM.get("level");
				intLastLevel = Integer.parseInt(strLastLevel);
				intLevel = Integer.parseInt(strLevel);
				strLastLevel = strLevel;
				if(intLastLevel==intLevel) {
					mlLastMapList.add(mPartEBOM);
				}
				if(intLevel>intLastLevel) {
					mLastMap = (Map)mlLastMapList.get(mlLastMapList.size()-1);
					mlChildMapList = new MapList(3);
					mlChildMapList.add(mPartEBOM);
					mLastMap.put("ChildStructure",mlChildMapList);
					mReferenceMap = new HashMap();
					mReferenceMap.put("Reference",mlLastMapList);
					mlReferenceList.add(mReferenceMap);
					mlLastMapList = mlChildMapList;
				} else if (intLevel<intLastLevel){
					mlTemp = new MapList();
					for(int i=0;i<intLevel;i++)
						mlTemp.add((Map)mlReferenceList.get(i));
					mlReferenceList = mlTemp;	
					mReferenceMap = (Map)mlReferenceList.get(mlReferenceList.size()-1);
					mlLastMapList = (MapList)mReferenceMap.get("Reference");
					mlLastMapList.add(mPartEBOM);
					mlReferenceList.remove(mlReferenceList.size()-1);
				}
			}
			MapList mlSortedList = new MapList(10);
			String strSortLevel = "2";
			if(mlEBOMDataToSort.size()>0) {
				Map mFirstMap = (Map)mlEBOMDataToSort.get(0);
				String strType = (String)mFirstMap.get("type");
				if(strType.equals("pgPhase"))
					strSortLevel = "1";
			}
			sortBOMStructure(mlEBOMDataToSort,mlSortedList,strSortLevel);
			ebomList=mlSortedList;
			
		//Added by DSM(Sogeti)2015x.2 To sort BOM structure based on Name for pgPhase and Find Number for childs for defect 7429 on 22-09-2016-End
		//369074

		HashMap hmTemp = new HashMap();

        hmTemp.put("expandMultiLevelsJPO","true");

		ebomList.add(hmTemp);

        }

      catch (FrameworkException Ex) {

        throw Ex;

        }



		return ebomList;

      }
//Added by DSM Sogeti for BOM table exception - Ends

//Added by DSM Sogeti for BOM table exception - Starts
    public MapList getStoredEBOMPDF(Context context, String[] args)

        throws Exception

    {

	//String sChildren = null;

     MapList ebomList = null;

     HashMap programMap = (HashMap)JPO.unpackArgs(args);

     String sMatchBasedOn = (String) programMap.get("matchBasedOn");

     try{

        ebomList = getEBOMsWithRelSelectables(context, args);



        Iterator itr = ebomList.iterator();

        MapList tList = new MapList();

        StringList ebomDerivativeList = EngineeringUtil.getDerivativeRelationships(context, RELATIONSHIP_EBOM, true);

        while(itr.hasNext())

        {

            HashMap newMap = new HashMap((Map)itr.next());

            newMap.put("selection", "multiple");

			//Added for hasChildren starts

			//sChildren = (String)newMap.get("from["+DomainConstants.RELATIONSHIP_EBOM+"]");

			//newMap.put("hasChildren",sChildren);

			newMap.put("hasChildren",EngineeringUtil.getHasChildren(newMap, ebomDerivativeList));

			//Added for hasChildren ends

            tList.add (newMap);

        }

        /*if(UIUtil.isNotNullAndNotEmpty(sMatchBasedOn)){

	        if("Find Number".equals(sMatchBasedOn)){

	        	tList.sort(EngineeringConstants.SELECT_FIND_NUMBER, "ascending",

				"integer");

	        }

	        if("Reference Designator".equals(sMatchBasedOn)){

	        	tList.sort("attribute["+DomainConstants.ATTRIBUTE_REFERENCE_DESIGNATOR+"]", "ascending",

				"real");

	        } 

        }*/

        ebomList.clear();

        ebomList.addAll(tList);

		//369074

		HashMap hmTemp = new HashMap();

        hmTemp.put("expandMultiLevelsJPO","true");

		ebomList.add(hmTemp);

        }

      catch (FrameworkException Ex) {

        throw Ex;

        }



		return ebomList;

      }
//Added by DSM Sogeti for BOM table exception - Ends
    /**
     * DSM (DS) 2015x.2 - Method to display values for Min and Max column on EBOM view
     * @param context
     * @param args
     * @return
     * @throws Exception
     */
    public Vector getMinMax(Context context,String args[]) throws Exception
    {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			MapList mlObjectList = (MapList) programMap.get("objectList");
	        HashMap columnMap = (HashMap)programMap.get("columnMap");
	        HashMap settings = (HashMap)columnMap.get("settings");
			String strAttirbuteName = (String)settings.get("Admin Type");
    	    Vector fnValuesVector = new Vector(mlObjectList.size());
    	    if(UIUtil.isNotNullAndNotEmpty(strAttirbuteName)){
    	    	strAttirbuteName = PropertyUtil.getSchemaProperty(context, strAttirbuteName);
				String strAttrSelect = "attribute["+strAttirbuteName+"]";
				String[] arrayId = new String[mlObjectList.size()];
				if(!mlObjectList.isEmpty())
				{
					Map mpObjectMap = new HashMap();
					for(int i=0;i< mlObjectList.size();i++)
					{
						mpObjectMap = (Map)mlObjectList.get(i);
						arrayId[i] = (String)mpObjectMap.get(DomainRelationship.SELECT_ID);
					}
				}
				if(arrayId != null && UIUtil.isNotNullAndNotEmpty(arrayId[0])&& arrayId.length>0)
				 {
					 ContextUtil.pushContext(context, PropertyUtil.getSchemaProperty(context, "person_UserAgent"), EMPTY_STRING, EMPTY_STRING);
					 MapList mlObjInfo = DomainRelationship.getInfo(context,arrayId, new StringList(strAttrSelect));
					 ContextUtil.popContext(context);
					 Map map = new HashMap();
					 if(!mlObjInfo.isEmpty() && mlObjInfo != null)
					 {
						 for(int j=0;j<mlObjInfo.size();j++)
						 {
							 map = (Map)mlObjInfo.get(j);
							 if(!map.isEmpty() && map != null)
							 {
								 String strValue = (String)map.get(strAttrSelect);
 								//DSM (DS) 2015x.4 - When the system calculates values in the formulation process the system shall only return a calculation answer up to 6 digits - START
								strValue = ${CLASS:pgDSOCommonUtils}.INSTANCE.getFormatedDecimalValue(strValue, ${CLASS:pgDSOConstants}.FORMAT_DECIMAL_SIX); 
								//DSM (DS) 2015x.4 - When the system calculates values in the formulation process the system shall only return a calculation answer up to 6 digits - END
								 fnValuesVector.addElement(strValue);
							 }
						 }
					 }
				 }
    		}
			return fnValuesVector;
	 }
	 
	 /**
	 * DSM(DS) 2015x.2 - This Method will return the flag for if the corressponding part are having the alternate parts or not
	 * in CPNENCEBOMIndentedSummary table
	 * @param context
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Vector getColumnAlternateData(Context context,String[] args) throws Exception
	{
		HashMap paramMap = (HashMap)JPO.unpackArgs(args);
		Map paramList = (Map) paramMap.get("paramList");
		String parentId = (String) paramList.get("objectId");
		MapList objectList = (MapList) paramMap.get("objectList");
		Vector<String> vcAlternateFlag = new Vector<String>(objectList.size());
		StringList slTypes =  new StringList();
		Map map = null;
		Map object = null;
		String objectId = DomainConstants.EMPTY_STRING;
		StringList strList  = new StringList(2);
		String sFromAlternate = DomainConstants.EMPTY_STRING;
		String sToAlternate = DomainConstants.EMPTY_STRING;
		//Modified by DSM(DS) for 2015x.4 ALM 13928 - Display Alternate column correctly for No Access parts in BOM - STARTS
		boolean bContextPushed = false;
		//Modified by DSM(DS) for 2015x.4 ALM 13928 - Display Alternate column correctly for No Access parts in BOM - ENDS
		
		String strIncludeTypes = FrameworkProperties.getProperty(context, "emxCPN.ProductDataTemplate.type_ProductData.TypeInclusionList");
		try{
			if(UIUtil.isNotNullAndNotEmpty(strIncludeTypes)){
				StringList slSymIncludeTypes = FrameworkUtil.split(strIncludeTypes,",");
				for(Object strtType : slSymIncludeTypes){
					slTypes.add((String)PropertyUtil.getSchemaProperty(context, strtType.toString()));
				}
			}
			if(UIUtil.isNotNullAndNotEmpty(parentId)){
				//Modified by DSM(DS) for 2015x.4 ALM 13928 - Display Alternate column correctly for No Access parts in BOM - STARTS
				ContextUtil.pushContext(context);
				bContextPushed = true;
				//Modified by DSM(DS) for 2015x.4 ALM 13928 - Display Alternate column correctly for No Access parts in BOM - ENDS
				DomainObject dObjParentObject = DomainObject.newInstance(context, parentId);
				String sType= dObjParentObject.getInfo(context,DomainConstants.SELECT_TYPE);
				if(slTypes.contains(sType)){
					for (Iterator iterator = objectList.iterator(); iterator.hasNext();)
					{
						object = (Map) iterator.next();
						objectId = (String) object.get("id");
						DomainObject dObj = DomainObject.newInstance(context, objectId);

						strList.add("from["+${CLASS:pgDSOConstants}.RELATIONSHIP_ALTERNATE+"]");
						strList.add("to["+${CLASS:pgDSOConstants}.RELATIONSHIP_ALTERNATE+"]");

						map = dObj.getInfo(context,strList);

						sFromAlternate = (String)map.get("from["+${CLASS:pgDSOConstants}.RELATIONSHIP_ALTERNATE+"]");
						
						//DSM(DS)-2015x.4- ALM:11128 Not to check to connection : Start
						//sToAlternate = (String)map.get("to["+${CLASS:pgDSOConstants}.RELATIONSHIP_ALTERNATE+"]");

						//if(${CLASS:pgDSOConstants}.VALUE_FALSE.equals(sFromAlternate) || ${CLASS:pgDSOConstants}.VALUE_TRUE.equals(sToAlternate))
						if(${CLASS:pgDSOConstants}.VALUE_FALSE.equals(sFromAlternate))
						//DSM(DS)-2015x.4- ALM:11128 Not to check to connection : End
						{
							vcAlternateFlag.add(${CLASS:pgDSOConstants}.INIT_CAPS_NO);
						}else{
							vcAlternateFlag.add(${CLASS:pgDSOConstants}.INIT_CAPS_YES);
						}

					}
				}
				//DSM(DS)-2015x.4- ALM:11123 to Add Vector value as NO if the Parent type is not present in the strIncludeTypes : Start
				else
					{
							vcAlternateFlag.add(${CLASS:pgDSOConstants}.INIT_CAPS_NO);
					}
				//DSM(DS)-2015x.4- ALM:11123 to Add Vector value as NO if the Parent type is not present in the strIncludeTypes : End	
			}

		}
		catch(Exception e){
			throw e;
		}
		//Modified by DSM(DS) for 2015x.4 ALM 13928 - Display Alternate column correctly for No Access parts in BOM - STARTS
		finally {
			if(bContextPushed) {
				ContextUtil.popContext(context);
			}
		}
		//Modified by DSM(DS) for 2015x.4 ALM 13928 - Display Alternate column correctly for No Access parts in BOM - ENDS
		return vcAlternateFlag;
	}
	
	 /**
		 * DSM(DS) 2015x.2 - This Method update Min max value on EBOM page		 
		 * @param context
		 * @param args
		 * @return boolean 
		 * @throws Exception
		 */
	    public boolean updateMinMax(Context context, String[] args) throws Exception
	    {
 	        HashMap programMap = (HashMap)JPO.unpackArgs(args);
	        HashMap paramMap = (HashMap)programMap.get("paramMap");
	        HashMap columnMap = (HashMap)programMap.get("columnMap");
	        HashMap settings = (HashMap)columnMap.get("settings");
	        String strAttirbuteName = (String)settings.get("Admin Type"); 
	        
	        boolean isUpdated = false;
	        if(UIUtil.isNotNullAndNotEmpty(strAttirbuteName))
	        {	
		        strAttirbuteName = PropertyUtil.getSchemaProperty(context, strAttirbuteName);				
		        String relId  = (String)paramMap.get("relId");
		        String newValue = (String)paramMap.get("New Value");
		        DomainRelationship domRel = new DomainRelationship(relId);
		        domRel.setAttributeValue(context, strAttirbuteName, newValue);
		        isUpdated = true;
	        }
	       
	        return isUpdated;
	  }
	    //DSM (DS) 2015x.4 ALM 13579 - Loss column is not editable when add NO Access items to BOM - starts
	    /**
		 * DSM(DS) 2015x.4 Gets the EBOM Attribute values to be populated for the EBOM table if the attribute name is passed to the method .
		 * @param context
		 * @param args
		 * @return Vector EBOM attribute values
		 */	
			public Vector getEBOMAttributeValues(Context context,String[] args) throws Exception
			{
				Vector fnValuesVector = new Vector();
				Map programMap = (Map)JPO.unpackArgs(args);
				MapList objectList = (MapList)programMap.get("objectList");
		        HashMap columnMap = (HashMap)programMap.get("columnMap");
		        HashMap settingsMap = (HashMap)columnMap.get("settings");
		        String strAttributeSymbName = (String)settingsMap.get("Admin Type");
				String strAttribValue = "";
				String strAttrName = PropertyUtil.getSchemaProperty(strAttributeSymbName);
				String strRelId ="";
				Map object = null;
				DomainRelationship dRelEBOM =null;
				for(Iterator itr = objectList.iterator(); itr.hasNext(); )
				{
					object = (Map) itr.next();
					strRelId = (String) object.get(DomainConstants.SELECT_RELATIONSHIP_ID);
					if(UIUtil.isNotNullAndNotEmpty(strRelId))
					{
						try
						{
							dRelEBOM = new DomainRelationship(strRelId);
							strAttribValue = (String)dRelEBOM.getAttributeValue(context,strAttrName);
							fnValuesVector.add(strAttribValue);
						}
						catch (Exception e)
						{
							fnValuesVector.add(DomainConstants.EMPTY_STRING);
							throw e;
						}
					}
					else
					{
						fnValuesVector.add(DomainConstants.EMPTY_STRING);
					}
				}
				return  fnValuesVector;
			}
		//DSM (DS) 2015x.4 ALM 13579 - Loss column is not editable when add NO Access items to BOM - ends
//DSM (DS) 2015x.4 ALM 14496 - Performance issue with Where Used on PMP - START
		/**This method is a Access Program to enable/disable column in the where used page depending upon if its a Flat table or structure browser.
		 *
		 * @param context is the matrix context
		 * @param args has the required information
		 * Since DSM (DS) 2015x.4 ALM 14496 - Performance issue with Where Used on PMP 
		 * @return
		 * @throws Exception
		 */
		public Boolean isFlatTable(Context context, String[] args) throws Exception {
			String languageStr = context.getSession().getLanguage();
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			String strTableMode = (String)programMap.get("tableMode");
			if(UIUtil.isNotNullAndNotEmpty(strTableMode) && "FlatTable".equals(strTableMode))
			{
				return true;
			}
			return false;
		}			
		 /**This method is a program to get the connected PDP to the phase in the where used page.
		 *
		 * @param context is the matrix context
		 * @param args has the required information
		 * Since DSM (DS) 2015x.4 ALM 14496 - Performance issue with Where Used on PMP 
		 * @return
		 * @throws Exception
		 */
		public Vector getPDPConnectedToPhase(Context context, String[] args) throws Exception {
			HashMap programMap = (HashMap) JPO.unpackArgs(args);
			Vector returnVector = new Vector();
			MapList mlObjectList = (MapList) programMap.get("objectList");
			StringBuffer tempValBuffer = new StringBuffer();
			String tempVal = DomainConstants.EMPTY_STRING;
			String strPhaseParentID = DomainConstants.EMPTY_STRING;
			Map objMap = null;
			if(mlObjectList != null && !mlObjectList.isEmpty())
    			{
	    			Iterator objListItr = mlObjectList.iterator();
	    			while (objListItr.hasNext())
	    			{
						tempValBuffer = new StringBuffer();
				        objMap = new HashMap();
	    				objMap = (Map) objListItr.next();
	    				if(objMap != null && !objMap.isEmpty())
	    				{
	    					strPhaseParentID = (String)objMap.get("PhaseParentID");
		    				if(UIUtil.isNotNullAndNotEmpty(strPhaseParentID))
		    				{
	   						    tempVal = tempValBuffer.append("<a href=\"../common/emxTree.jsp?objectId=").append(strPhaseParentID).append("\" target=\"popup\">").append((String)objMap.get("PhaseParentName")).append("</a>").toString();
		    				}
		    				else
		    				{
		    					tempVal = DomainConstants.EMPTY_STRING;
		    				}
	    				}
	    				returnVector.add(tempVal);
	    			}
    			}
			return returnVector;
		}
		//DSM (DS) 2015x.4 ALM 14496 - Performance issue with Where Used on PMP - END
	
	//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - START
	/**
	* Overridden from emxECPartBase  and modified for allowing all rel attributes to be copied in Copy To Functionality
    */

     @com.matrixone.apps.framework.ui.ConnectionProgramCallable
     public  HashMap inlineCreateAndConnectPart(Context context, String[] args) throws Exception{

		HashMap doc = new HashMap();
		HashMap request = (HashMap) JPO.unpackArgs(args);
		HashMap paramMap = (HashMap) request.get("paramMap");
		//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - START
		String strRelTemp = "";
		Map relMap  =new HashMap();
		Map attributeMap  =new HashMap();		
		String strRelIds = (String)paramMap.get("relIds");
		if(UIUtil.isNotNullAndNotEmpty(strRelIds)){
		strRelIds = strRelIds.substring(0, strRelIds.length() - 1);
		StringList slRelIds = FrameworkUtil.split(strRelIds,",");
		String strTemprelId = "";
		String strToId = "";
		for(int j=0; j<slRelIds.size(); j++){
			strTemprelId = (String)slRelIds.get(j);
			DomainRelationship domRel = new DomainRelationship(strTemprelId);
			domRel.openRelationship(context);
			strToId = domRel.getTo().getObjectId();
			domRel.closeRelationship(context, true);
			relMap.put(strToId,strTemprelId);
		}
		}
		//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - END
		HashMap hmRelAttributesMap;
		HashMap columnsMap;
        HashMap changedRowMap;
        HashMap returnMap;
        String sType = (String)paramMap.get("type");
        String sSymbolicName = com.matrixone.apps.framework.ui.UICache.getSymbolicName(context, sType, "type");

		Map smbAttribMap;

		Element elm = (Element) request.get("contextData");

		MapList chgRowsMapList = UITableIndented.getChangedRowsMapFromElement(context, elm);
		MapList mlItems = new MapList();

 		String strRelType = (String) paramMap.get("relType");
 		String parentObjectId = (String) request.get("parentOID");
 		String rowFormat = "";
 		String CONNECT_AS_DERIVED = EnoviaResourceBundle.getProperty(context, "emxEngineeringCentral.ReplaceBOM.Derived");
        String vpmControlState = null;
        String sUser = context.getUser();String objectName = "";
        String vName = "";
        String strComponentLocation = "";
        String[] attributeKeys = { DomainConstants.ATTRIBUTE_FIND_NUMBER,
				DomainConstants.ATTRIBUTE_REFERENCE_DESIGNATOR,
				DomainConstants.ATTRIBUTE_COMPONENT_LOCATION,
				DomainConstants.ATTRIBUTE_QUANTITY,
				DomainConstants.ATTRIBUTE_USAGE,
				DomainConstants.ATTRIBUTE_NOTES };


        StringList sResultList = new StringList();
        String tokValue = "";
        String tok = "";
        String sResult ="";
        String parentBusType = "";
        BusinessType busType = null;
        if(UIUtil.isNotNullAndNotEmpty(sSymbolicName)){
        sResult = MqlUtil.mqlCommand(context, "temp query bus $1 $2 $3 select $4 dump","eService Object Generator",sSymbolicName,"*","revision");
        while(UIUtil.isNullOrEmpty(sResult)) { 
       	 busType = new BusinessType(sType, context.getVault());
       	 if (busType != null){
       		 parentBusType = busType.getParent(context);
       		 if (UIUtil.isNotNullAndNotEmpty(parentBusType))
       		 {
       			 sType = parentBusType;
       			 sSymbolicName = com.matrixone.apps.framework.ui.UICache.getSymbolicName(context, sType, "type");
       			 sResult = MqlUtil.mqlCommand(context, "temp query bus $1 $2 $3 select $4 dump","eService Object Generator",sSymbolicName,"*","revision");
       			 if(UIUtil.isNotNullAndNotEmpty(sResult))
       			 {
       				break;
       			 }
       		 }
       	 }
        }
        }
        StringTokenizer stateTok = new StringTokenizer(sResult, "\n");
        while (stateTok.hasMoreTokens())
        	{
       	 	tok = (String)stateTok.nextToken();
       	 	tokValue = tok.substring(tok.lastIndexOf(',')+1);
       	 	sResultList.add(tokValue);
        	}
        int sResultListSize = sResultList.size();
        StringList objAutoNameList = new StringList();
        
        for(int i=0; i<sResultListSize; i++){
        	objAutoNameList.add(UINavigatorUtil.getI18nString("emxEngineeringCentral.Common."+((String)sResultList.get(i)).replace(" ", ""), "emxEngineeringCentralStringResource", "en"));
        }

        boolean isENGSMBInstalled = EngineeringUtil.isENGSMBInstalled(context, false); //Commented for IR-213006

        if (isENGSMBInstalled) { //Commented for IR-213006
        	String mqlQuery = new StringBuffer(100).append("print bus $1 select $2 dump").toString();
        	vpmControlState = MqlUtil.mqlCommand(context, mqlQuery,parentObjectId,"from["+RELATIONSHIP_PART_SPECIFICATION+"|to.type.kindof["+EngineeringConstants.TYPE_VPLM_CORE_REF+"]].to.attribute["+EngineeringConstants.ATTRIBUTE_VPM_CONTROLLED+"]");
		}

        //ContextUtil.pushContext(context);

		try
		{
			DomainObject parentObj = DomainObject.newInstance(context, parentObjectId);
			DomainObject childObj;

 			DomainRelationship domRelation;

 			EBOMMarkup ebomMarkup = new EBOMMarkup();

 			for (int i = 0, size = chgRowsMapList.size(); i < size; i++) {
 				try {
 					changedRowMap = (HashMap) chgRowsMapList.get(i);

 					String childObjectId = (String) changedRowMap.get("childObjectId");
 					String sRelId = (String) changedRowMap.get("relId");
		
 					String sRowId = (String) changedRowMap.get("rowId");
 					rowFormat = "[rowId:" + sRowId + "]";
 					String markup = (String) changedRowMap.get("markup");
 					String strParam2 = (String) changedRowMap.get("param2");
 					// get parameters for replace operation
 					String strParam1 = (String) changedRowMap.get("param1");

					columnsMap = (HashMap) changedRowMap.get("columns");

					String strUOM = (String) columnsMap.get("UOM");
					String desc = (String) columnsMap.get("Description");

					hmRelAttributesMap = getAttributes(columnsMap, attributeKeys);
					strComponentLocation = getStringValue(columnsMap, DomainConstants.ATTRIBUTE_COMPONENT_LOCATION);
					if(UIUtil.isNotNullAndNotEmpty(strComponentLocation)) {
						columnsMap.put(DomainConstants.ATTRIBUTE_COMPONENT_LOCATION, StringUtils.replace(strComponentLocation,"&", "&amp;"));
					}
					String vpmVisible = "";
					// TBE
            		 if (isENGSMBInstalled) { //Commented for IR-213006
    					vpmVisible = getStringValue(columnsMap, "VPMVisible");
    					 //If part is not in VPM Control set the isVPMVisible value according to user selection.
						if (isValidData(vpmVisible) && !"true".equalsIgnoreCase(vpmControlState))
    						hmRelAttributesMap.put(EngineeringConstants.ATTRIBUTE_VPM_VISIBLE, vpmVisible);
            		}
            		// TBE

            		changedRowMap.put("parentObj", parentObj);

 					if (MARKUP_ADD.equals(markup)) {
 						childObj = DomainObject.newInstance(context, childObjectId);
						
                        changedRowMap.put("childObj", childObj);
                        changedRowMap.put("strRelType", strRelType);

                        domRelation = ebomMarkup.connectToChildPart(context, changedRowMap);
						
						if (isValidData(desc)) {
							childObj.setDescription(context, desc);
						}
						
						//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - START
						if(relMap.containsKey(childObjectId)){
							strRelTemp = (String)relMap.get(childObjectId);
							attributeMap= DomainRelationship.getAttributeMap(context, strRelTemp);
							attributeMap.putAll(hmRelAttributesMap);
						}
						//domRelation.setAttributeValues(context, hmRelAttributesMap);
						domRelation.setAttributeValues(context, attributeMap);
						//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - END

 						if ("true".equalsIgnoreCase(CONNECT_AS_DERIVED) && isValidData(strParam2)) {
 							StringList slParamObjs = childObj.getInfoList(context, SELECT_FROM_DERIVED_IDS);

 							if (slParamObjs == null || !slParamObjs.contains(strParam2)) {
 								DomainRelationship doRelDerived = DomainRelationship.connect(context, new DomainObject(strParam2), RELATIONSHIP_DERIVED, childObj);
 								doRelDerived.setAttributeValue(context, ATTRIBUTE_DERIVED_CONTEXT, "Replace");
 							}
 						}

						sRelId = domRelation.toString();
					}


 					else if (MARKUP_NEW.equals(markup)) {

						objectName = (String) columnsMap.get("Name");
						String objectType = (String) columnsMap.get("Type");
						String objectRev = (String) columnsMap.get("Revision");
						String objectPolicy = (String) columnsMap.get("Policy");
						String objectVault = (String) columnsMap.get("Vault");
						String objectPartFamily = (String) columnsMap.get("Part Family");

						smbAttribMap = new HashMap();
						smbAttribMap.put(DomainConstants.ATTRIBUTE_UNIT_OF_MEASURE, strUOM);
						smbAttribMap.put(DomainConstants.ATTRIBUTE_ORIGINATOR, sUser);

						// TBE
						if (isENGSMBInstalled) { //Commented for IR-213006
        					vName = getStringValue(columnsMap, "V_Name");
        					vName = UIUtil.isNullOrEmpty(vName)?(String) columnsMap.get("V_Name1"):vName;
        					if (isValidData(vName))
        						smbAttribMap.put(EngineeringConstants.ATTRIBUTE_V_NAME, vName);
        					if (isValidData(vpmVisible) && !"true".equalsIgnoreCase(vpmControlState))
        						smbAttribMap.put(EngineeringConstants.ATTRIBUTE_VPM_VISIBLE, "TRUE");
        				}
        			    // TBE

						// Use Part Family for naming the Part - Start

						if (isValidData(objectPartFamily)) {
							PartFamily partFamilyObject = null;
							try {
						        partFamilyObject = new PartFamily(objectPartFamily);
						        partFamilyObject.open(context);

						        if (partFamilyObject.exists(context)) {
						            // check the "Part Family Name Generator On" attribute
									String usePartFamilyForName = partFamilyObject.getAttributeValue(context, ATTRIBUTE_PART_FAMILY_NAME_GENERATOR_ON);

						            if ("TRUE".equalsIgnoreCase(usePartFamilyForName)) {
						                objectName = partFamilyObject.getPartFamilyMemberName(context);
						            }
						        }
						    } finally {
						        if (partFamilyObject != null) {
						            partFamilyObject.close(context);
						        }
						    }
						}

						// Use Part Family for naming the Part - End

						childObj = DomainObject.newInstance(context);
						childObj = createchildObj(context, objectType, objectName, objectRev, objectPolicy, context.getVault().getName(), childObj, objAutoNameList.contains(objectName));

						// parts created with inline had owner - user agent. removing if condition to change owner.
						childObj.setOwner(context, sUser);

						if (isValidData(desc)) {
							childObj.setDescription(context, desc);
						}
						childObj.setAttributeValues(context, smbAttribMap);

                        changedRowMap.put("childObj", childObj);
                        changedRowMap.put("strRelType", strRelType);

                        domRelation = ebomMarkup.connectToChildPart(context, changedRowMap);
 						domRelation.setAttributeValues(context, hmRelAttributesMap);

						/* Connecting Part Family with Part starts */
						if (isValidData(objectPartFamily)) {
							DomainObject PartFamilyObj = DomainObject.newInstance(context, objectPartFamily);
							DomainRelationship.connect(context, PartFamilyObj, DomainConstants.RELATIONSHIP_CLASSIFIED_ITEM, childObj);
						}
						/* Connecting Part Family with Part ends */

						//Added RDO Convergence start
						String strDefaultRDO = childObj.getAltOwner1(context).toString();
						String defaultRDOId = MqlUtil.mqlCommand(context, "temp query bus $1 $2 $3 select $4 dump $5",DomainConstants.TYPE_ORGANIZATION,strDefaultRDO,"*","id","|");
			            defaultRDOId = defaultRDOId.substring(defaultRDOId.lastIndexOf('|')+1);
			            DomainRelationship.connect(context,
								new DomainObject(defaultRDOId), // from side object Design Responsibilty
								DomainConstants.RELATIONSHIP_DESIGN_RESPONSIBILITY, // Relationship
								childObj);// toSide object Document

			          //Added RDO Convergence End

						childObjectId = childObj.getId();
						sRelId = domRelation.toString();

					}


 					else if (MARKUP_CUT.equals(markup)) {
 						if (!"replace".equals(strParam1)) {
 							ebomMarkup.disconnectChildPart(context, changedRowMap);
 						}
 					}

					returnMap = new HashMap();
					returnMap.put("pid", parentObjectId);
					returnMap.put("relid", sRelId);
					returnMap.put("oid", childObjectId);
					returnMap.put("rowId", sRowId);
					returnMap.put("markup", markup);
					objectName = (String)columnsMap.get("Name");
					if(objectName != null && !"null".equals(objectName) && !"".equals(objectName)) {
						columnsMap.put("Name", StringUtils.replace(objectName,"&", "&amp;"));
					}
					if(isENGSMBInstalled) { //Commented for IR-213006
						vName = getStringValue(columnsMap, "V_Name");
						if(vName != null)
							columnsMap.put("V_Name", StringUtils.replace(vName,"&", "&amp;"));
					}

					returnMap.put("columns", columnsMap);

					mlItems.add(returnMap); // returnMap having all the

 				} catch (Exception e) {
 					if (e.toString().indexOf("license") > -1) {
 						throw e;
 					}
 					throw new Exception(rowFormat + e);
 				}

 			}
 			doc.put("Action", "success"); // Here the action can be "Success" or
 			// "refresh"
 			doc.put("changedRows", mlItems);// Adding the key "ChangedRows"
 		} catch (Exception e) {
 			doc.put("Action", "ERROR"); // If any exception is there send "Action" as "ERROR"

 			if (e.toString().indexOf("license") > -1) { // If any License Issue throw the exception to user.
 				doc.put("Message", rowFormat);
 				throw e;
 			}

 			if ((e.toString().indexOf("recursion")) != -1) {
 				//Multitenant
 				String recursionMesssage = EnoviaResourceBundle.getProperty(context, "emxEngineeringCentralStringResource", context.getLocale(),"emxEngineeringCentral.RecursionError.Message");
 				doc.put("Message", rowFormat + recursionMesssage);
 			}
				//Multitenant
 			else if ((e.toString().indexOf("recursion")) == -1 && ((e.toString().indexOf("Check trigger")) != -1)) {

 				String tnrMesssage = EnoviaResourceBundle.getProperty(context, "emxEngineeringCentralStringResource", context.getLocale(),"emxEngineeringCentral.TNRError.Message");
 				doc.put("Message", rowFormat + tnrMesssage);
 			}

 			else {
 				String strExcpn = e.toString();
 				int j = strExcpn.indexOf(']');
 			    strExcpn = strExcpn.substring(j + 1, strExcpn.length());
 				doc.put("Message", rowFormat + strExcpn);
 			}

		} finally {
			//ContextUtil.popContext(context);
		}

		return doc;

	}
	/**
	* Overridden from emxECPartBase as this method is Private and modified for allowing all rel attributes to be copied in Copy To Functionality
    */
	private HashMap getAttributes(HashMap map, String[] keys) throws Exception {
    	 int length = length (keys);
    	 HashMap mapReturn = new HashMap(length);
    	 String data;
    	 for (int i = 0; i < length; i++) {
    		 data = getStringValue(map, keys[i]);
    		 if (isValidData(data)) {
    			 mapReturn.put(keys[i], data);
    		 }
    	 }
    	 return mapReturn;
     }
	//DSM (DS) 2015X.5 - ALM 9855 : "Copy to" action isn't working properly - END
	
	//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
	//Overloaded from base class emxECPartBase - added additional argument to identify if refinements are applied
	/** Returns all the parent parts where this object is connected as Substitute according to the Revision selected in the revision filter.
	 * @param context ematrix context
	 * @param objectId
	 * @param objectSelect selectable to select from object
	 * @param relSelect selectable to select from object
	 * @param objectWhere object where clause to be passed to getRelatedObjects
	 * @param shRecurseToLevel short value to determine level for Where Used expansion
	 * @return MapList
	 * @throws Exception if any exception occurs.
	 */
	public MapList getEbomSustituteParts(Context context, String objectId, StringList objectSelect, StringList relSelect,String objectWhere, short shRecurseToLevel) throws Exception
	{
		String strMqlQuery = "print bus $1 select $2 dump $3";
		String strSubPartIds = MqlUtil.mqlCommand(context, strMqlQuery, objectId, "relationship["+RELATIONSHIP_EBOM_SUBSTITUE+"].fromrel.to.id","|");

		MapList ebomSubList = null;

		if (isValidData(strSubPartIds)) {
			String whereExpr = "frommid[" + RELATIONSHIP_EBOM_SUBSTITUE + "].to.id == " + objectId;

			StringList slSubPartIds = FrameworkUtil.split(strSubPartIds, "|");
			HashSet hSetUniqueId = new HashSet();

			int size = slSubPartIds.size();

			MapList listTemp;
			ebomSubList = new MapList(size);

			DomainObject domObj;
			String parentObjectId;
			//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
			short shLevelRecurse = 0;
			String strParentType = DomainConstants.EMPTY_STRING;
			String strParentId = DomainConstants.EMPTY_STRING;
			String strLevel = DomainConstants.EMPTY_STRING;
			Map mpParent = null;
			Map mTempPart = null;
			MapList mlParentList = null;
			MapList mlNLParentList = null;
			DomainObject domObjParent = null;
			objectSelect.add(DomainConstants.SELECT_TYPE);
			//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS

			for (int i = 0; i < size; i++) {
				parentObjectId = (String) slSubPartIds.get(i);
				if (hSetUniqueId.add(parentObjectId)) {
					domObj = DomainObject.newInstance(context, parentObjectId);

				listTemp = domObj.getRelatedObjects(context,
								DomainConstants.RELATIONSHIP_EBOM,
								DomainConstants.TYPE_PART,
								objectSelect,
								relSelect,
								true,
								false,
								(short) 1,
									objectWhere,
								whereExpr);
								
				//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute STARTS
				if(listTemp != null && !listTemp.isEmpty()) {//Display additional levels for EBOM Substitute based on user refinements
					int iExpLevel = Integer.parseInt(Short.toString(shRecurseToLevel));
					if(iExpLevel != 0 && iExpLevel != -1) {
						//if User has refined to mention specific level value (2,3, etc) - then set expand level as userinput - 1
						//since one level is already expanded to retrieve EBOM Substitute
						shLevelRecurse = (short)(iExpLevel - 1);
					} else {
						//if User has refined to select All or Highest, then set expand level the same
						shLevelRecurse = (short)iExpLevel;
					}
					mlParentList = new MapList();
					for (int j = 0; j < listTemp.size(); j++) {
						mpParent = (Map)listTemp.get(j);
						strParentType = (String)mpParent.get(DomainConstants.SELECT_TYPE);
						strParentId = (String)mpParent.get(DomainConstants.SELECT_ID);
						//mlParentList = new MapList();
						domObjParent = DomainObject.newInstance(context,strParentId);
						//retrieve EBOM parents from the Substitute parent
							//DSM(DS) 2018x.1.1 - ALM#24567 - INC2544282 - Where-used expansion with filter does not show eBOM substitutes at levels >=2. - Start
							if(iExpLevel !=1) {
								mlNLParentList = domObjParent.getRelatedObjects(context,
										DomainConstants.RELATIONSHIP_EBOM,
										DomainConstants.TYPE_PART,
										objectSelect,
										relSelect,
										true,
										false,
										shLevelRecurse,
										objectWhere,
										null);
							}
							mpParent.put("relationship", "EBOM Substitute");
							//DSM(DS) 2018x.1.1 - ALM#24567 - INC2544282 - Where-used expansion with filter does not show eBOM substitutes at levels >=2. - End
						mlParentList.add(mpParent);
						if(mlNLParentList != null && !mlNLParentList.isEmpty()) {
							for (int k=0;k < mlNLParentList.size();k++) {
								mTempPart = (Map)mlNLParentList.get(k);
								strLevel = (String)mTempPart.get("level");
								int iTemp = Integer.parseInt(strLevel);
								//since EBOM Substitute is first level, hence incrementing further EBOM levels by 1
								iTemp +=1;
								strLevel = Integer.toString(iTemp);
								mTempPart.put("level",strLevel);
								//add the EBOM where used levels to maplist
								mlParentList.add(mTempPart);
							}
						}
					}
					if(mlParentList != null && !mlParentList.isEmpty())
					{
							listTemp = new MapList();
							listTemp.addAll(mlParentList);
					}
				}
				//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS

				ebomSubList.addAll(listTemp);
				}
			}
		}

		return ebomSubList;
	}
	//DSM(DS) ALM 17390 - Where Used fix to display multi-levels for EBOM Substitute ENDS
	
	//DSM(DS) 2018x.0 ALM 22129 - Performance improvement for CSS BOM page loading - STARTS
	//Overridden from base class
	/*
	  * Retrieves the EBOM data. Method is added to expand the ebom and fetch the
	  * Latest or Latest Complete nodes in child.
	  *
	  * @param context the ENOVIA <code>Context</code> object
	  * @param HashMap paramMap
	  * @throws Exception if error encountered while carrying out the request
	  */

 	public MapList expandEBOM(Context context, HashMap paramMap) throws Exception {  //name modified from getEBOMsWithRelSelectablesSB
		//HashMap paramMap = (HashMap) JPO.unpackArgs(args);

		int nExpandLevel = 0;
		String partId = getStringValue(paramMap, "objectId");
		String sExpandLevels = getStringValue(paramMap, "emxExpandFilter");
		String selectedFilterValue = getStringValue(paramMap, "ENCBOMRevisionCustomFilter");
		String strAttrEnableCompliance = PropertyUtil.getSchemaProperty(context, "attribute_EnableCompliance");
		String complete = PropertyUtil.getSchemaProperty(context, "policy", DomainConstants.POLICY_DEVELOPMENT_PART, "state_Complete");
		String release = PropertyUtil.getSchemaProperty(context, "policy", DomainConstants.POLICY_EC_PART, "state_Release");
		String SELECT_ATTR_ENABLE_COMPLIANCE = "attribute[" + strAttrEnableCompliance + "]";
		String curRevision;
		String latestObjectId;
		String latestRevision;

		if (!isValidData(selectedFilterValue)) {
			selectedFilterValue = "As Stored";
		}

		if (!isValidData(sExpandLevels)) {
			sExpandLevels = getStringValue(paramMap, "ExpandFilter");
		}
		
		String SELECT_LAST_EBOM_EXISTS = "last.from[EBOM]";

		StringList objectSelect = createStringList(new String[] {SELECT_ID, SELECT_TYPE,SELECT_REVISION, SELECT_LAST_ID, SELECT_LAST_REVISION,SELECT_LAST_CURRENT,
																		SELECT_REL_FROM_EBOM_EXISTS, SELECT_ATTR_ENABLE_COMPLIANCE, SELECT_LAST_EBOM_EXISTS});
		//BOM UI Performance: Attributes required for Related Physical title column
		String attrVPLMVName = PropertyUtil.getSchemaProperty(context,"attribute_PLMEntity.V_Name");
        attrVPLMVName = "attribute["+attrVPLMVName+"]";
        String typeVPLMProd = PropertyUtil.getSchemaProperty(context,"type_PLMEntity");
        
        String selectPartVName = "attribute["+EngineeringConstants.ATTRIBUTE_V_NAME+"]";
        //String selectProdctIdSel = "from["+DomainConstants.RELATIONSHIP_PART_SPECIFICATION+"|to.type.kindof["+typeVPLMProd+"]].to."+DomainConstants.SELECT_ID;
        String selectProdctIdSel = "from["+DomainConstants.RELATIONSHIP_PART_SPECIFICATION+"].to["+typeVPLMProd+"]."+DomainConstants.SELECT_ID;
        String selectPrdPhysicalId = "from["+DomainConstants.RELATIONSHIP_PART_SPECIFICATION+"].to["+EngineeringConstants.TYPE_VPLM_VPMREFERENCE+"].physicalid";
        String sVPLMInstanceRelId ="frommid["+EngineeringConstants.RELATIONSHIP_VPM_PROJECTION_RELID+"].torel.physicalid";
        
	    objectSelect.add(selectPrdPhysicalId);
		objectSelect.add(selectProdctIdSel);
		objectSelect.add(SELECT_PRODUCT_NAME);
		objectSelect.add(selectPartVName);
		objectSelect.add(SELECT_PRODUCT_ATTR_USAGE);
		//BOM UI Performance: Attributes required for Related Physical title column
		//DSM(DS) 2018x.0 ALM 22129 - Performance improvement for CSS BOM page loading - STARTS
		/*StringList relSelect = createStringList(new String[] {SELECT_RELATIONSHIP_ID, SELECT_ATTRIBUTE_FIND_NUMBER, sVPLMInstanceRelId, SELECT_ATTRIBUTE_UNITOFMEASURE, SELECT_ATTRIBUTE_REFERENCE_DESIGNATOR, SELECT_ATTRIBUTE_QUANTITY, BOMMgtConstants.SELECT_ATTRIBUTE_ISVPMVISIBLE,
				 BOMMgtConstants.SELECT_EBOM_INSTANCE_DESCRIPTION, BOMMgtConstants.SELECT_EBOM_INSTANCE_TITLE});*/
		StringList relSelect = createStringList(new String[] {SELECT_RELATIONSHIP_ID, SELECT_ATTRIBUTE_FIND_NUMBER, sVPLMInstanceRelId, SELECT_ATTRIBUTE_UNITOFMEASURE, SELECT_ATTRIBUTE_REFERENCE_DESIGNATOR, SELECT_ATTRIBUTE_QUANTITY, BOMMgtConstants.SELECT_ATTRIBUTE_ISVPMVISIBLE,
				 BOMMgtConstants.SELECT_EBOM_INSTANCE_DESCRIPTION, BOMMgtConstants.SELECT_EBOM_INSTANCE_TITLE, "from.type", "from.id"});
		//DSM(DS) 2018x.0 ALM 22129 - Performance improvement for CSS BOM page loading - ENDS

		if (!isValidData(sExpandLevels) || ("Latest".equals(selectedFilterValue) || "Latest Complete".equals(selectedFilterValue) ||"Latest Release".equals(selectedFilterValue))) {
			nExpandLevel = 1;
			partId = getStringValue(paramMap, "partId") == null? partId : getStringValue(paramMap, "partId");
		} else if ("All".equalsIgnoreCase(sExpandLevels)) {
			nExpandLevel = 0;
		} else {
			nExpandLevel = Integer.parseInt(sExpandLevels);
		}
		
		Part partObj = new Part(partId);
		IBOMFilterIngress iBOMFilterIngress = IBOMFilterIngress.getService();
		String[] relSelectArr = new String[relSelect.size()];
		String[] objSelectArr =  new String[objectSelect.size()];
		relSelectArr = (String[]) relSelect.toArray(relSelectArr);
		objSelectArr = (String[]) objectSelect.toArray(objSelectArr);
		iBOMFilterIngress.setExpandLevel(String.valueOf(nExpandLevel));
		iBOMFilterIngress.setObjectSelect(objSelectArr);
		iBOMFilterIngress.setRelationshipSelect(relSelectArr);
		
		
		IBOMService ibomService = IBOMService.getService(context, partId, false);
		MapList ebomList = ibomService.getBOM(context, iBOMFilterIngress);
		/*MapList ebomList = partObj.getRelatedObjects(context,
				          							 RELATIONSHIP_EBOM,
				          							 TYPE_PART,
				          							 objectSelect,
				          							 relSelect,
					                                 false,
					                                 true,
					                                 (short) nExpandLevel,
					                                 null, null, 0);*/

		  Iterator itr = ebomList.iterator();
		  Map newMap;

		  StringList ebomDerivativeList = EngineeringUtil.getDerivativeRelationships(context, RELATIONSHIP_EBOM, true);
		  
		  String LATEST = EnoviaResourceBundle.getProperty(context, "emxEngineeringCentralStringResource", new Locale("en"), "emxEngineeringCentral.RevisionFilterOption.Latest"); 
		  String LATEST_RELEASED = EnoviaResourceBundle.getProperty(context, "emxEngineeringCentralStringResource", new Locale("en"), "emxEngineeringCentral.RevisionFilterOption.Latest_Release"); 
		  
	      if (LATEST.equals(selectedFilterValue) || ("Latest Complete".equals(selectedFilterValue)) || (LATEST_RELEASED.equals(selectedFilterValue))) {
	          //Iterate through the maplist and add those parts that are latest but not connected

	          while (itr.hasNext()) {
	              newMap = (Map) itr.next();

	              curRevision    = getStringValue(newMap, SELECT_REVISION);
	              latestObjectId = getStringValue(newMap, SELECT_LAST_ID);
	              latestRevision = getStringValue(newMap, SELECT_LAST_REVISION);

	              if (nExpandLevel != 0) {	            	  
		        	  newMap.put("hasChildren", EngineeringUtil.getHasChildren(newMap, ebomDerivativeList));
		        	  newMap.put(EngineeringConstants.INITIAL_LOAD, "true");
	              }

	              if ("Latest".equals(selectedFilterValue)) {
	            	  newMap.put(SELECT_ID, latestObjectId);
	            	  newMap.put("hasChildren", EngineeringUtil.getHasChildren(newMap, ebomDerivativeList));
		        	  newMap.put(EngineeringConstants.INITIAL_LOAD, "true");
	              }

	              else {
	                   DomainObject domObjLatest = DomainObject.newInstance(context, latestObjectId);
	                   String currSta = domObjLatest.getInfo(context, DomainConstants.SELECT_CURRENT);

	                   if (curRevision.equals(latestRevision)) {
	                	   if (complete.equalsIgnoreCase(currSta) || release.equalsIgnoreCase(currSta)) {
	                		   newMap.put(SELECT_ID, latestObjectId);
	                		   newMap.put(EngineeringConstants.INITIAL_LOAD, "true");
	                	   } else {
	                		   itr.remove();
	                	   }
	                   }
	                   else {
	                	   while(true) {
	                   		   if(currSta.equalsIgnoreCase(complete) || currSta.equalsIgnoreCase(release)) {
	                   			   newMap.put(SELECT_ID, latestObjectId);
	                   			   newMap.put(EngineeringConstants.INITIAL_LOAD, "true");
	                   			   	break;
	                   		   } else {
	                   			   BusinessObject boObj = domObjLatest.getPreviousRevision(context);
	                   			   if(!(boObj.toString()).equals("..") ) {
	                   				   boObj.open(context);
	                   				latestObjectId = boObj.getObjectId();
	                   				   domObjLatest = DomainObject.newInstance(context,latestObjectId);
	                        		   currSta = domObjLatest.getInfo(context,DomainConstants.SELECT_CURRENT);
	                   			   } else {
	                   				   itr.remove();
	                   				   break;
	                   			}
	                   		 }
	                  	  }//End of while
	                   }//End of Else
	               }
	            }//End of While

	      	}//End of IF, Latest or Latest complete filter is selected

	      else if (nExpandLevel != 0) {
	          while (itr.hasNext()) {
	        	  newMap = (Map) itr.next();

	        	  // To display  + or - in the bom display	        	  
	        	  newMap.put("hasChildren", EngineeringUtil.getHasChildren(newMap, ebomDerivativeList));
	        	  newMap.put(EngineeringConstants.INITIAL_LOAD, "true");
	          }
	      }
	      else{		  
				  while (itr.hasNext()) {
					newMap = (Map) itr.next();
					newMap.put(EngineeringConstants.INITIAL_LOAD, "true");
	    	  }
	      }
	 	return ebomList;
	 }
	//DSM(DS) 2018x.0 ALM 22129 - Performance improvement for CSS BOM page loading - ENDS
 	
 	/**
	 * Overrided method from base JPO emxECPartBase
	 * Modified For APOLLO - To remove single selection for Mass Substitutes Assignments functionality
 	 * Added for IR-044514V6R2011
     * This method is to disply all child objects for substitute functionality
     * @param context
     * @param args
     * @return MapList
     * @throws Exception
     */
    @com.matrixone.apps.framework.ui.ProgramCallable
    public MapList getBOMStructure(Context context, String[] args) throws Exception
    {

        HashMap paramMap = (HashMap)JPO.unpackArgs(args);
        String partId = (String)paramMap.get("objectId");
		//APOLLO - Modified for Mass Substitute Assignments - START
        String mode = (String)paramMap.get("mode");
		//APOLLO - Modified for Mass Substitute Assignments - END
        Part partObj = new Part(partId);
        MapList ebomList = new MapList();
        StringList selectStmts = new StringList(6);
        StringList selectRelStmts = new StringList(6);
        selectStmts.addElement(DomainConstants.SELECT_ID);
        selectStmts.addElement(DomainConstants.SELECT_TYPE);
        selectStmts.addElement(DomainConstants.SELECT_NAME);
        selectStmts.addElement(DomainConstants.SELECT_REVISION);
        selectStmts.addElement(DomainConstants.SELECT_DESCRIPTION);
        selectRelStmts.addElement(DomainConstants.SELECT_RELATIONSHIP_ID);
        selectRelStmts.addElement(SELECT_ATTRIBUTE_REFERENCE_DESIGNATOR);
        selectRelStmts.addElement(SELECT_ATTRIBUTE_QUANTITY);
        selectRelStmts.addElement(SELECT_ATTRIBUTE_FIND_NUMBER);
        selectRelStmts.addElement(SELECT_ATTRIBUTE_COMPONENT_LOCATION);
        selectRelStmts.addElement(SELECT_ATTRIBUTE_USAGE);
        ebomList = partObj.getRelatedObjects(context,
                						DomainConstants.RELATIONSHIP_EBOM,  		// relationship pattern
                						DomainConstants.TYPE_PART,                  // object pattern
                                        selectStmts,                 				// object selects
                                        selectRelStmts,              				// relationship selects
                                        false,                        				// to direction
                                        true,                       				// from direction
                                        (short)1,                    				// recursion level
                                        null,     									// object where clause
                                        null);                       				// relationship where clause
		//APOLLO - Modified for Mass Substitute Assignments - START
        if(!"massSubstitutes".equals(mode))
        {
		//APOLLO - Modified for Mass Substitute Assignments - END
        	Iterator itr = ebomList.iterator();
        	MapList tList = new MapList();
        	while(itr.hasNext())
        	{
            	HashMap newMap = new HashMap((Map)itr.next());
            	newMap.put("selection", "single");          
            	tList.add (newMap);
        	}
        	ebomList.clear();
        	ebomList.addAll(tList);		
		}  // End of If loop - APOLLO - Modified for Mass Substitute Assignments

        return ebomList;
	}
    // DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - STARTS
    //Overridden from the base JPO to get the exclude objects to which logged in user does'nt have access 
    @Override
    /**
	 * Returns a StringList of the object ids which are connected using Part Specification Relationship and objects revisions ids
	 * for a given context.
	 * @param context the eMatrix <code>Context</code> object.
	 * @param args contains a packed HashMap containing objectId of object
	 * @return StringList.
     * @since EngineeringCentral X3
	 * @throws Exception if the operation fails.
	*/
	
	@com.matrixone.apps.framework.ui.ExcludeOIDProgramCallable
	public StringList excludeOIDPartSpecificationConnectedItems(Context context, String[] args) throws Exception {
    	// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - STARTS
		StringList result = new StringList();
		boolean isContextPushed = false;
		try{
			// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - ENDS
		HashMap programMap = (HashMap) JPO.unpackArgs(args);
        String  parentObjectId = (String) programMap.get("objectId");
     // DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - STARTS
        //StringList result = new StringList();
     // DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - ENDS
        if (parentObjectId == null)
        {
        	return (result);
        }
	    DomainObject domObj = new DomainObject(parentObjectId);
		String strTypeCADDrawing = PropertyUtil.getSchemaProperty(context,"type_CADDrawing");
		String strTypeCADModel = PropertyUtil.getSchemaProperty(context,"type_CADModel");
		String strTypeDrawingPrint = PropertyUtil.getSchemaProperty(context,"type_DrawingPrint");
		String strTypePartSpecification = PropertyUtil.getSchemaProperty(context,"type_PartSpecification");
		String strTypeTechnicalSpecification = PropertyUtil.getSchemaProperty(context,"type_TechnicalSpecification");
		String strTypeViewable = PropertyUtil.getSchemaProperty(context,"type_Viewable");
		StringBuffer sbTypePattern = new StringBuffer(strTypeCADDrawing);
              sbTypePattern.append(',');
              sbTypePattern.append(strTypeCADModel);
              sbTypePattern.append(',');
              sbTypePattern.append(strTypeDrawingPrint);
			  sbTypePattern.append(',');
              sbTypePattern.append(strTypePartSpecification);
			  sbTypePattern.append(',');
              sbTypePattern.append(strTypeTechnicalSpecification);
			  sbTypePattern.append(',');
              sbTypePattern.append(strTypeViewable);
		String relToExpand = PropertyUtil.getSchemaProperty(context,"relationship_PartSpecification");
		StringList selectStmts  = new StringList(1);
        selectStmts.addElement(DomainConstants.SELECT_ID);
		// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - STARTS
        ContextUtil.pushContext(context,PropertyUtil.getSchemaProperty(context,"person_UserAgent"),DomainConstants.EMPTY_STRING,DomainConstants.EMPTY_STRING);
        isContextPushed = true;
		// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - ENDS
	    MapList mapList = domObj.getRelatedObjects(context,
													relToExpand,              // relationship pattern
													sbTypePattern.toString(), // object pattern
													selectStmts,              // object selects
													null,                     // relationship selects
													false,                    // to direction
													true,                     // from direction
													(short) 1,                // recursion level
													null,                     // object where clause
													null);                    // relationship where clause
            Iterator i1 = mapList.iterator();
            while (i1.hasNext())
            {
                Map m1 = (Map) i1.next();
				String strId = (String)m1.get(DomainConstants.SELECT_ID);
				DomainObject dObj = new DomainObject(strId);

				MapList revmapList = dObj.getRevisionsInfo(context, selectStmts, new StringList());
			    Iterator i2 = revmapList.iterator();
				while (i2.hasNext()){
				Map m2 = (Map) i2.next();
				String strIds = (String)m2.get(DomainConstants.SELECT_ID);
				result.addElement(strIds);
				}
            }
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
			
		}
		// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - STARTS
		finally
		{
			if(isContextPushed)
			{
				ContextUtil.popContext(context);
			}
		}
		// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - ENDS
		return result;
	}
    
	// DSM (DS) 2018x.2 ALM 28855 Can't add data to Specifications one by one in Catia MPAP - ENDS
}

