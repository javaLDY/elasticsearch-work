package cn.baiing.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.baiing.measure.quantity.ClockRate;
import cn.baiing.measure.quantity.DataRate;
import cn.baiing.measure.quantity.Duration;
import cn.baiing.measure.quantity.ElectricCharge;
import cn.baiing.measure.quantity.Geography;
import cn.baiing.measure.quantity.Length;
import cn.baiing.measure.quantity.PriceByTime;
import cn.baiing.measure.quantity.ScreenSize;
import cn.baiing.measure.quantity.TrafficPrice;
import cn.baiing.measure.quantity.TrafficSpeed;
import cn.baiing.measure.quantity.Weight;

public enum Unit {
	
	//Length
    METER("m", true, 1000, Length.class.getName()),
    CENTIMETER("cm", false, 10,  Length.class.getName()),
    KILOMETER("km", false, 1000*1000,  Length.class.getName()),
    MILIMETER("mm", false, 1,  Length.class.getName()),
    //ElectricCharge
    AMPERHOUR("Ah", false, 1000, ElectricCharge.class.getName()),
    MILLIAMPERHOUR("mAh", true, 1, ElectricCharge.class.getName()),
    //DataRate
    BYTE("B", true, 1, DataRate.class.getName()),
    KILOBYTE("KB", false, 1024, DataRate.class.getName()),
    MEGABYTE("MB", false, 1024*1024, DataRate.class.getName()),
    GIGABYTE("GB", false, 1024*1024*1024, DataRate.class.getName()),
    TERABYTE("TB", false, 1024d*1024*1024*1024, DataRate.class.getName()),   
    //TrafficPrice
    RMBPERKB("元/KB", true, 1024*1024, TrafficPrice.class.getName()),
    RMBPERMB("元/MB", false, 1024, TrafficPrice.class.getName()),
    RMBPERGB("元/GB", false, 1, TrafficPrice.class.getName()),    
    //TrafficSpeed
    KBPERSECOND("Kbps", true, 1, TrafficSpeed.class.getName()),
    MBPERSECOND("Mbps", false, 1024, TrafficSpeed.class.getName()),
    GBPERSECOND("Gbps", false, 1024*1024, TrafficSpeed.class.getName()),
    //PriceByTime
    RMBPERMINUTE("元/分", true,1, PriceByTime.class.getName()), // NOTE : rate error here.
    RMBPERHOUR("元/小时", false, 60, PriceByTime.class.getName()),
    RMBPERDAY("元/天", false, 60*24, PriceByTime.class.getName()),
    RMBPEMONTH("元/月", false, 60*24*30, PriceByTime.class.getName()),
    RMBPERQUATER("元/季度", false, 60*24*30*3, PriceByTime.class.getName()),
    RMBPERHALFYEAR("元/半年", false,  60*24*30*6, PriceByTime.class.getName()),
    RMBPEREAR("元/年", false,  60*24*30*12, PriceByTime.class.getName()),
    RMBPER2YEAR("元/2年", false,  60*24*30*12*2, PriceByTime.class.getName()),
    RMBPER3YEAR("元/3年", false,  60*24*30*12*3, PriceByTime.class.getName()),
    //Duration
    SECOND("秒", true, 1, Duration.class.getName()),
    MINUTE("分钟", true, 60, Duration.class.getName()),
    HOUR("小时", true, 60*60, Duration.class.getName()),
    DAY("天", true, 60*60*24, Duration.class.getName()),
    MONTH("月", true, 60*60*24*30, Duration.class.getName()),
    QUATER("季度", true, 60*60*24*30*3, Duration.class.getName()),
    HALFYEAR("半年", true, 60*60*24*30*6, Duration.class.getName()),
    ONEYEAR("年", true, 60*60*24*30*12, Duration.class.getName()),
    TOWYEAR("2年", true, 60*60*24*30*24, Duration.class.getName()),
    THREEYEAR("3年", true, 60*60*24*30*36, Duration.class.getName()),
    //Weight
    GRAM("g", true, 1, Weight.class.getName()),
    KILOGRAM("kg", true, 1000, Weight.class.getName()),

    //Screen Size
    SCREENINCH("inch", true, 1, ScreenSize.class.getName()),
    //geography
    NATIONWIDE("全国", false, 1, Geography.class.getName()),//nationwide
    PROVINCE("本省", false, 1, Geography.class.getName()),
    CITY("本市", false, 1, Geography.class.getName()),
    AREA("本区", false, 1, Geography.class.getName()),
    SCHOOL("本校", false, 1, Geography.class.getName()),
    //ClockSpeed
    MEGAERTZ("MHz", true, 1000*1000, ClockRate.class.getName()),
    HERTZ("Hz", false, 1, ClockRate.class.getName()),
    KILO("KHz", false, 1000, ClockRate.class.getName()),
    GIGAERTZ("GHz", false, 1000*1000*1000, ClockRate.class.getName());

    Unit(String name, boolean isStandard, double conversionRate, String measureClassName)
    {
        this.name = name;
        this.isStandard = isStandard;
        this.conversionRate = conversionRate;
        this.measureClassName = measureClassName;
    }    

    private String name;
    private boolean isStandard;
    private double conversionRate;
    private String measureClassName;
    private static Map<String, List<Unit>> measureUnitCombiner = new HashMap<String, List<Unit>>();
    private static Map<String, Unit> measureUnit = new HashMap<String, Unit>();
    private static int currentValue = 0;
    static{
    	if(measureUnit == null || measureUnit.isEmpty()){
    		for(Unit unit : Unit.values())
            {
                measureUnit.put(unit.getName(), unit);
            }
    	}
    }   

    public static List<Unit> getUnitsByMeasureClassName(String measureClassName )
    {
        return measureUnitCombiner.get(measureClassName);
    }
    
    public static Unit getMeasureUnit(String unit)
    {
        return measureUnit.get(unit);
    }
    
    public static int parse(String value, String currentUnit) {  
    	if(measureUnit.containsKey(currentUnit)){
    		Unit unit = measureUnit.get(currentUnit);
    		currentValue = (int)(Double.valueOf(value) / unit.getConversionRate());
    	}
    	return currentValue;
    }
    
    public String getName() {
        return name;
    }
    public boolean isStandard() {
        return isStandard;
    }
    public double getConversionRate() {
        return conversionRate;
    }
    public String getMeasureClassName() {
        return measureClassName;
    }   

}
