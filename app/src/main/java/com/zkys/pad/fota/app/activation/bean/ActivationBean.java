package com.zkys.pad.fota.app.activation.bean;

public class ActivationBean{

    /**
     * deptName : 手足外科
     * code : 8986011726580058759
     * deptId : 1
     * pageSize : 20
     * hospitalName : 东莞市人民医院
     * type : 2
     * pageNum : 1
     * hospitalId : 1
     * mobileNumber : 13244998442
     * activetion : 1
     * name : Pad_1006
     * disabled : 1
     * id : 135
     * bedNumber : 1006
     * simCode : 862433552968257
     */



    private int id;

    //"唯一编号")

    private String code;

    //"1:已激活，0：未激活")

    private int activetion;

    //"设备名")

    private String name;

    //"厂商")

    private String company;

    //"型号")
    private String modelNumber;

    //"屏幕尺寸")

    private String screenSize;

    //"1:可用，0：不可用")

    private int disabled;

    //"pad在极光注册设备标识")

    private String jpushRegistid;

    //"1:商用，2：测试机，3：样机")

    private int type;

    //"SIM卡串码")

    private String simCode;

    //"SIM卡号码")

    private String simMobile;

    //"支架id")

    private String kickstandCode;

    //"输液设备id")

    private String transfusionCode;

    //"备注")

    private String remark;

    //"绑定记录id，没有绑定默认0")

    private int bindingId;

    //"病床id，拓展字段")

    private int bedId;

    //"病床号，拓展字段")

    private String bedNumber;

    //"病床所属医院id，拓展字段")

    private int hospitalId;

    //"病床所属医院名称，拓展字段")

    private String hospitalName;

    //"病床所属科室id，拓展字段")

    private int deptId;

    //"病床所属科室名称，拓展字段")

    private String deptName;

    //"签名key")
    private String pad;
    private String host;
    private String active;
    private String iccid;

    public String getIccid() {
        return iccid == null ? "" : iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getActive() {
        return active == null ? "" : active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getHost() {
        return host == null ? "" : host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getActivetion() {
        return activetion;
    }

    public void setActivetion(int activetion) {
        this.activetion = activetion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getJpushRegistid() {
        return jpushRegistid;
    }

    public void setJpushRegistid(String jpushRegistid) {
        this.jpushRegistid = jpushRegistid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSimCode() {
        return simCode;
    }

    public void setSimCode(String simCode) {
        this.simCode = simCode;
    }

    public String getSimMobile() {
        return simMobile;
    }

    public void setSimMobile(String simMobile) {
        this.simMobile = simMobile;
    }

    public String getKickstandCode() {
        return kickstandCode;
    }

    public void setKickstandCode(String kickstandCode) {
        this.kickstandCode = kickstandCode;
    }

    public String getTransfusionCode() {
        return transfusionCode;
    }

    public void setTransfusionCode(String transfusionCode) {
        this.transfusionCode = transfusionCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBindingId() {
        return bindingId;
    }

    public void setBindingId(int bindingId) {
        this.bindingId = bindingId;
    }

    public int getBedId() {
        return bedId;
    }

    public void setBedId(int bedId) {
        this.bedId = bedId;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPad() {
        return pad;
    }

    public void setPad(String pad) {
        this.pad = pad;
    }

}
