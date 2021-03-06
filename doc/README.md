# JSON Configuration

The JSON config file consists of several major sections (objects on the first level of the JSON schema):

## Main sections

### **TEMPLATE_INFO** section 
Defines basic info for the data format being imported. Intended mainly for internal usage. This section contains the options: **NAME**, **VERSION** and **TYPE**.

### **DATA_ACCESS** section 
defines the basic access to the excel file

### **PARALLEL_SHEETS** section 
is used to set up simultaneous access to several sheets of a given excel file.
 
### **SUBSTANCE_RECORD** section 
Defines the reading (data locations) of the basic fields of a substance record. 

### **PROTOCOL_APPLICATIONS** section 
Defines how to read an array of ProtocolApplication objects which are included in the SubtsanceRecord object defined in the previous section.

## **Excel Data Location** 
is a key concept for the JSON configuration of excel parser reader. Excel data location is required for each data component of the Substance representation e.g. `PUBLIC_NAME`, `CITATION_TITLE`, `ENDPOINT` etc.

The Excel data location is defined by means of several options:

### **ITERATION** field 
Defines the iteration mode - how the data from this location is accessed. The fFollowing iteration modes are supported: 

   *ROW_SINGLE* - data is accessed treating each excel table row as a separate data unit (e.g. Substance record),

   *ROW_MULTI_FIXED* - a fixed number of rows are treated as a separate Substance record,

   *ROW_MULTI_DYNAMIC* - a dynamic number of rows are used to load a Substance record (the number of rows may vary for each record),

   *ABSOLUTE_LOCATION* - the data component is read from absolute location fro the excel file e.g. sheet, row and column must be defined

   *JSON_VALUE* - the data component is taken directly from the JSON config file

   *JSON_REPOSITORY* - the data component is taken directly from the JSON config file but special section REPOSOTORY is used.

### Fields: **SHEET_INDEX** **COLUMN_INDEX** and **ROW_INDEX** 
define respectively excel sheet, column and row for reading data from a single excel cell. Depending on the ITERATION mode, some of these fields are not required (if supplied they are ignored). For example in *ROW_SINGLE* mode only column index will be used while in ABSOLUTE_LOCATION mode all of the above inddices will be used. When particular index is needed for the current iteration mode but not supplied, a parsing error is obtained.
Typically the default values of the fields **ITERATION** and **SHEET_INDEX** (when not supplied explicitly) are taken globally from the **DATA_ACCESS** section for the primary iteration sheet or from the corresponding **PARALLEL_SHEET**.

Excel data location may define an array of cells. For this purpose boolean JSON field **IS_ARRAY** is set to *true* (default value if missing is *false*).
Accordingly fields: **ROW_INDICES** and **COLUMN_INDICES** are used to define arrays of row and column indices. Since all row/column indices are given explicitly it is not needed to be consequent numbers hence more complex sets of cells can set this way. 

Column indices may be defined in two ways by integers (designating the column numbers) or as column labels (as used in the Excel standart addressing). For example: "COLUMN_INDICES" :[2,3,4] or "COLUMN_INDICES" :["B","C","D"] are both valid specifications.  

## Section details

### DATA_ACCESS section 

Defines the basic parameters for data access and iteration of the primary sheet. This section describes the basic approach for reading data i.e. these are the default reading parameters. When particular parameter in a given Excel Data Location is ommited the default value is taken from this section. 
Data access for other sheets (addional secondary sheets) is set in JSON array section *PARALLEL_SHEETS* analogously to this section.

**ITERATION**	Defines the iteration mode. Possible iteration modes were already described above:
ROW_SINGLE, ROW_MULTI_FIXED, ROW_MULTI_DYNAMIC, ABSOLUTE_LOCATION, JSON_VALUE, JSON_REPOSITORY, VARIABLE. 

**SHEET_INDEX**	The primary sheet for iteration

**SHEET_NAME**	The primary sheet name

**START_ROW**	The starting row for iteration

**START_HEADER_ROW**	The first (starting) header row

**END_HEADER_ROW**	The last (ending) header row

**ALLOW_EMPTY**	Flag that defines whether empty cells are allowed. Default value is true 

**RECOGNITION**	The mode for sheet/column/row recognition. These elements can be recognized by index or by name.

**DYNAMIC_ITERATION**	Defines how dynamic iteration is performed in mode ROW_MULTI_DYNAMIC. Several rows are read at once where the criterion for row group recognition is: NEXT_NOT_EMPTY or NEXT_DIFFERENT_VALUE.

**DYNAMIC_ITERATION_COLUMN_INDEX**	The index of the column used for dynamic iteration.

**VARIABLES**	is a subsection that defines an array of excel locations that are read and stored into a work list of variables later used in the reading process. The variables can be used for more complex routing of data e.g. one value can be used/loaded in several components of the substance record.


### PARALLEL_SHEETS 

This is an array of sections similar to section *DATA_ACCESS* that define the simultaneous reading of several sheets together with the primary sheet. 


### SUBSTANCE_RECORD section

Defines the excel locations for reading of the basic fields of a Substance Record: 

`SUBSTANCE_NAME`, `OWNER_NAME`, `SUBSTANCE_TYPE`, `OWNER_UUID`, `SUBSTANCE_UUID`, `PUBLIC_NAME`, `COMPOSITION`
 
### PROTOCOL_APPLICATIONS section

This is an array of sections , defining the excel data locations for reading of Protocol Application data. Each section includes following fields:  `CITATION_TITLE`, `CITATION_YEAR`, `CITATION_OWNER`,  `INTERPRETATION_RESULT`, `INTERPRETATION_CRITERIA`, `PROTOCOL_GUIDELINE`,`PARAMETERS` (an array of data locations), `EFFECTS` (an array of sections)

#### **EFFECTS [ ]**	
This is an array of sections. Each section defines data structures (effect record) for particular measurements and includes following excel data locations: `SAMPLE_ID`, `ENDPOINT`, `LO_VALUE`, `UP_VALUE`, `ERR_VALUE`, `TEXT_VALUE`, `VALUE`, `LO_QUALIFIER`, `UP_QUALIFIER`, `ERR_QUALIFIER`, `UNIT`, `CONDITIONS` (an array of data locations)

#### `REPOSITORY`	A JSON structure for defining preconfigured data (e.g. protocol, parameters) to be read directly from the JSON file into the data classes. 
