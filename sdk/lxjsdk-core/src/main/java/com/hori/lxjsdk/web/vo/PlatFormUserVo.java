package com.hori.lxjsdk.web.vo;

/**
 * 第三方平台用户vo
 * @author sucs
 *
 */
public class PlatFormUserVo {


	/**
	 * 数据的唯一标识id,在将第三方平台地址转换为对讲地址时由系统生成,因同一user有可能对应多套房，一套房下有可能有多个user,所以用userId+vdcsAddress作为id
	 */
	private String id;
	
	/**
	 * 第三方平台用户id
	 */
	private String userId;
	

	private String address;
	
	/**
	 * 姓名
	 */
	private String userName;
	
	/**
	 * 小区名
	 */
	private String areaName;
	
	/**
	 * 小区区域名
	 */
	private String regionName;
	
	/**
	 * 楼栋名
	 */
	private String buildingName;
	
	/**
	 * 单元名
	 */
	private String unitName;
	
	/**
	 * 楼层
	 */
	private String floor;
	
	/**
	 * 房间
	 */
	private String room;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 存放转换后的地址(对讲上的住房地址)
	 */
	private String vdcsAddress;
	
	/**
	 * pms人员id
	 */
	private String peopleId;
	
	/**
	 * 住房序列号
	 */
	private String householdSerial;
	
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
	/**
	 * 匹配到的住房
	 */
	private HouseholdVo householdVo; 
	
	public String getUserId() {
		return userId;
	}
	public String getAddress() {
		return address;
	}
	public String getUserName() {
		return userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getVdcsAddress() {
		return vdcsAddress;
	}
	public void setVdcsAddress(String vdcsAddress) {
		this.vdcsAddress = vdcsAddress;
	}
	public HouseholdVo getHouseholdVo() {
		return householdVo;
	}
	public void setHouseholdVo(HouseholdVo householdVo) {
		this.householdVo = householdVo;
	}
	public String getPeopleId() {
		return peopleId;
	}
	public void setPeopleId(String peopleId) {
		this.peopleId = peopleId;
	}
	public String getHouseholdSerial() {
		return householdSerial;
	}
	public void setHouseholdSerial(String householdSerial) {
		this.householdSerial = householdSerial;
	}
	public String getAreaName() {
		return areaName;
	}
	public String getRegionName() {
		return regionName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public String getUnitName() {
		return unitName;
	}
	public String getFloor() {
		return floor;
	}
	public String getRoom() {
		return room;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public void setRoom(String room) {
		this.room = room;
	}
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
	public void setId(String id) {
		this.id = id;
	}
}
