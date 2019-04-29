package com.miu360.taxi_check.data;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lubao.lubao.async.ExceptionHandler;
import com.lubao.lubao.async.Result;
import com.miu360.inspect.R;
import com.miu360.taxi_check.App;
import com.miu360.taxi_check.model.Driver;
import com.miu360.taxi_check.model.NetCarQrInfo;
import com.miu360.taxi_check.model.NetPersonQrInfo;

public class HtmlDataEweiweima {

	public static Result<Driver> getDriverInfo(String data) {
		Result<Driver> result = new Result<>();
		try {
			Driver d = new Driver();
			Document res = Jsoup.parse(data);
			Element h4 = res.select(".media-heading").get(0).getElementsByTag("span").get(1);
			String name = h4.text();
			d.setName(name);
			Elements h6 = res.select(".media-body").get(0).getElementsByTag("h6");
			for (Element element : h6) {
				String html = element.html();
				if (html.contains("监督卡号")) {
					String driver_licence = element.getElementsByTag("span").get(1).text();
					d.setDriverLicence(driver_licence);
				} else if (html.contains("公司名称")) {
					String company = element.getElementsByTag("span").get(1).text();
					d.setCompany(company);
				} else if (html.contains("性别")) {
					String sex = element.getElementsByTag("span").get(1).text();
					d.setSex(sex);
					;
				} else if (html.contains("防伪码")) {
					String fangweima = element.getElementsByTag("span").get(1).text();
					d.setFangweima(fangweima);
				} else if (html.contains("公司联系电话")) {
					String phoneNumber = element.getElementsByTag("span").get(1).text();
					d.setPhoneNumber(phoneNumber);
				}
			}
			try {
				d.setHead(res.getElementById("photo").absUrl("src"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.setData(d);
		} catch (Exception e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.qr_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}
		return result;

	}


	public static Result<NetCarQrInfo> getCarInfo(String data) {
		Result<NetCarQrInfo> result = new Result<>();
		try {
			NetCarQrInfo d = new NetCarQrInfo();
			Document res = Jsoup.parse(data);
			Element h4 = res.select(".media-heading").get(0).getElementsByTag("span").get(1);
			String name = h4.text();
			d.setVName(name);
			Elements h6 = res.select(".media-body").get(0).getElementsByTag("h6");
			for (Element element : h6) {
				String html = element.html();
				if (html.contains("车辆所有人")) {
					d.setCarOwn(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("车型")) {
					d.setCarModel(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("车身颜色")) {
					d.setCarColor(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("平台名称")) {
					d.setPlatformName(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("车辆注册登记日期")) {
					d.setDate(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("车架号")) {
					d.setVIN(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("发动机号")) {
					d.setEngineNO(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("车辆状态")) {
					d.setCarState(element.getElementsByTag("span").get(1).text());
				} else if (html.contains("防伪码")) {
					d.setSecurityCode(element.getElementsByTag("span").get(1).text());
				}
			}
			result.setData(d);
		} catch (Exception e) {
			e.printStackTrace();
			result.setError(-1);
			result.setErrorMsg(App.self.getString(R.string.qr_err));
			result.setThrowable(e);
			ExceptionHandler.handleException(App.self, result);
		}
		return result;

	}


}
