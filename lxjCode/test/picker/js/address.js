var picker = new mui.PopPicker({layer:3});
var picker1 = new mui.PopPicker({layer:1});
var pickerStreet = new mui.PopPicker({layer:1});
var vue = new Vue({
	el:".dd",
	data:{
		expressageName: '',
		addressBig: '',
		addressStreet: '',
		addressItem: [],
		type: 1,
		provinceCode: '',
		cityCode: '',
		areaCode: '',
		parentCode: '',
		pickerFlag: false,
		nodesSelectNum: '',
		ctmsHost: 'https://tt.hori-gz.com:8443',
		token: "15221984995716d6b6d7ce2443ce9644"
	},
	methods:{
		expressage: function(){
			
			picker1.setData([{
		        value: '110000',
		        text: '京东'
		    },{
		        value: '110000',
		        text: '京东'
		    }])
			picker1.pickers[0].setSelectedIndex(1, 1000,function(){});
			picker1.show(function (selectItems) {
			    vue.expressageName = selectItems[0].text;//智子
			})
		},
		addressBigFunc: function(){
			this.pickerFlag = true;
			if(this.pickerFlag){
				this.areaPickerAjax(this.type,this.parentCode);	
			}
		},
		addressStreetFunc: function(){
			this.areaPickerAjax(2,this.areaCode,3);
		},
		addressPicker: function(pickerAddress){	
			picker.setData(pickerAddress);
			picker.show(function (selectItems) {
		    	vue.addressBig = selectItems[0].text + selectItems[1].text + selectItems[2].text;
		    	vue.areaCode = selectItems[2].value;
		    	vue.pickerFlag = false;
		    	if(vue.expressageName != vue.addressStreet){
		    		vue.addressStreet = "";
		    	}
			});
		},
		areaPicker: function(){	
			if(this.pickerFlag){
				console.log(picker.getSelectedItems()[1].children);
				this.areaPickerAjax(2,picker.getSelectedItems()[0].value,1);
			}
		},
		areaPickerAjax: function(type,parentCode,nodes){
			var params = {
				header: {
					token: this.token,
					time_stamp: new Date().getTime()
				},
				body: {
					type: type,
					parentCode: parentCode
				}
			};
			var paramsStr = encodeURI(JSON.stringify(params));
            var httpURL = vue.ctmsHost+"/mms/servlet/getAdministrativeDivision?str="+paramsStr;
                this.$http.jsonp(httpURL,{
				emulateJSON: true,
				method: "get",
				dataType: "jsonp",
				jsonp: "jsoncallback",
				jsonpCallback: "success_jsonpCallback"
			}).then(function(res) {	
				var addressItemArray = JSON.parse(JSON.stringify(res.body.areaList).replace(/code/g, "value").replace(/name/g, "text"));
				if(type == 1 && nodes != 3){
					vue.addressItem = addressItemArray;
					this.addressPicker(vue.addressItem);
					this.areaPickerAjax(2,picker.getSelectedItems()[0].value,1);
					this.provinceCode = picker.getSelectedItems()[0].value;

				}else if(type == 2){
					if(nodes == 1){
						for(var i = 0; i < vue.addressItem.length; i++){
							if(vue.addressItem[i].value == parentCode){
								vue.addressItem[i].children = addressItemArray;
							}
						}
						this.addressPicker(vue.addressItem);
						this.cityCode = picker.getSelectedItems()[0].children[0].value;
						if(picker.getSelectedItems()[1].value != undefined){
							this.cityCode = picker.getSelectedItems()[1].value;
						}
						this.areaPickerAjax(2,vue.cityCode,2);
					}
					if(nodes == 2){
						this.provinceCode = picker.getSelectedItems()[0].value;
						vue.nodesSelectNum = picker.getSelectedItems()[1].value;
						//console.log(picker.getSelectedItems());
						for(var i = 0; i < vue.addressItem.length; i++){
							if(vue.addressItem[i].value == vue.provinceCode){
								for(var j = 0; j < vue.addressItem[i].children.length; j++){
									if(vue.addressItem[i].children[j].value == picker.getSelectedItems()[1].value){
										var areaItem = vue.addressItem[i].children[j];
										Vue.set(vue.addressItem[i].children[j],"children",addressItemArray);
										vue.addressPicker(vue.addressItem);
									}
									if(vue.addressItem[i].children[j].children  != undefined){
										if(JSON.stringify((vue.addressItem[i].children[j].children)) ==JSON.stringify(addressItemArray)){
											return;
										}
										Vue.set(vue.addressItem[i].children[j],"children",addressItemArray);
										vue.addressPicker(vue.addressItem);
										console.log(111111);
										console.log(vue.addressItem[i].children[j].children);
									}
								}
							}
						}
					}
					if(nodes == 3){
						pickerStreet.setData(addressItemArray);
						pickerStreet.show(function (selectItems) {
					    	vue.addressStreet = selectItems[0].text;
						});
					}
				}
				
			},function(res){});		
		}
	},
	mounted: function(){
		
	}
});