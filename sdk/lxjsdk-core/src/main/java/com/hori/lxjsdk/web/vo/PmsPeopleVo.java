package com.hori.lxjsdk.web.vo;

public class PmsPeopleVo {

	private String id;
	
	private String userId;
	
	private String userName;
	
	private String mobile;

	private String householdSerial;
	
	private String address;
	
	private String areaCode;
	
	private String xyid;
	
	private String regionSerial;
	
	private Integer peopleType;
	
	private String pmsHouseholdAddressId;
	/**
	 * 性别（选填），0：表示男 1：表示女 2：未知
	 */
	private String sex;
	/**
	 * 民族（选填），系统默认"汉族"
	 */
	private String nation;
	/**
	 * 民族代号（选填），系统默认"01"
	 */
	private String nationCode;
	/**
	 * 身份证号码（选填）
	 */
	private String idCard;
	/**
	 * 户籍地址（选填），不超过200个字符
	 */
	private String registerAddress;
	/**
	 * 信息录入方式（选填），1：读卡录入，2：手工录入
	 */
	private String infoEnteringType;
	/**
	 * 入住日期（选填），格式"yyyy-MM-dd"
	 */
	private String liveDate;
	/**
	 * 居住方式（选填），0:不详,1:单身居住,2:合伙居住,3:家庭居住,4:集体居住,5:其它
	 */
	private String liveWay;
	/**
	 * 入住状态（选填），1:入住，0：注销
	 */
	private String liveStatus;
	/**
	 * 更新日期（选填），格式"yyyy-MM-dd HH:mm:ss"
	 */
	private String updateTime;
	/**
	 * 注销日期（选填），格式"yyyy-MM-dd HH:mm:ss"
	 */
	private String logoutTime;
	
	public String getSex() {
		return sex;
	}

	public String getNation() {
		return nation;
	}

	public String getNationCode() {
		return nationCode;
	}

	public String getIdCard() {
		return idCard;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public String getInfoEnteringType() {
		return infoEnteringType;
	}

	public String getLiveDate() {
		return liveDate;
	}

	public String getLiveWay() {
		return liveWay;
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public String getLogoutTime() {
		return logoutTime;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public void setInfoEnteringType(String infoEnteringType) {
		this.infoEnteringType = infoEnteringType;
	}

	public void setLiveDate(String liveDate) {
		this.liveDate = liveDate;
	}

	public void setLiveWay(String liveWay) {
		this.liveWay = liveWay;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getHouseholdSerial() {
		return householdSerial;
	}

	public void setHouseholdSerial(String householdSerial) {
		this.householdSerial = householdSerial;
	}

	public String getXyid() {
		return xyid;
	}

	public void setXyid(String xyid) {
		this.xyid = xyid;
	}

	public String getRegionSerial() {
		return regionSerial;
	}

	public void setRegionSerial(String regionSerial) {
		this.regionSerial = regionSerial;
	}

	public Integer getPeopleType() {
		return peopleType;
	}

	public void setPeopleType(Integer peopleType) {
		this.peopleType = peopleType;
	}

	public String getPmsHouseholdAddressId() {
		return pmsHouseholdAddressId;
	}

	public void setPmsHouseholdAddressId(String pmsHouseholdAddressId) {
		this.pmsHouseholdAddressId = pmsHouseholdAddressId;
	}
}
