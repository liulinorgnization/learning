﻿Ext.define('crm.view.work.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.work-main',
    returnArrayStr: function(obj){
        var str = "";
        //判断传入的对象是否是数组
        if(typeof obj=='object'){
            if(obj!=undefined&&obj!=null&&obj!=""){
                if(obj.length>0){
                    str = obj[0].id;
                    if(obj.length>1){
                        for(var i=1;i<obj.length;i++){
                            str = str+","+obj[i].id;
                        }
                    }
                }
            }
        }else if(typeof obj=='string'){
            if(obj!=undefined&&obj!=null&&obj!=""){
                str = obj;
            }
        }
        if(str=="")str="123"
        return str;
    },
    initViewModel: function (vm) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        crm.user.info({}, function (r, m, s) {
            if (s) {
                me.userId = r.userId;
                me.comId = r.companyId;
                //团队数据加载
                me.teamDataload(me.userId);
                //代办事项
                me.matterDataload(me.userId);
                    //销售客户统计  日期 部门 成员
                me.salesDayId = 0;
                me.salesDepartmentId = null;
                me.salesUserId = null;

                 //销售客户一览
                me.glanceDay = 0;
                me.glanceDeptId = null;
                me.glanceUserId = null;
                //设置权限
                me.setAuthority(me.userId);
            
                //销售客户统计
                me.salesDataload(me.userId, me.salesDayId, me.salesDepartmentId, me.salesUserId);
            
                me.getGlanceData(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
                
                
                var myDomain = window.location.host;
		         var pathName = window.document.location.pathname;
		         var timestamp=Date.parse(new Date());
		         
		         
		         var app = crm.getApplication();
		         var viewport = app.viewport;
		         var vm_root = viewport.getViewModel();
		         var loginUserId = me.userId;
		         var loginCompanyId = me.comId;
		         var str = loginCompanyId+"_"+loginUserId;
		         webSocket=new WebSocket("ws://"+myDomain+"/websocket/"+str);
		         console.info("ws://" + myDomain + "/websocket/" + str);
		         webSocket.onerror = function(event) {
		         	console.info("错误"+event.data);
		         };
		         webSocket.onopen = function(event) {
		            //document.getElementById("list").innerHTML="连接建立成功！";
		             console.info("连接建立成功");
		         };
		         webSocket.onmessage = function(event) {
		         	var noticeStore = vm.getStore("notice");
		         	if(noticeStore!=null){
		         		var messages = [];
			         	if(event.data!=""&&event.data!=undefined){
			         		var messageData = Ext.util.JSON.decode(event.data);
			         		//加载所有未读
			         		if(messageData.result=="0"){
					         	Ext.Array.each(messageData.data,function(item,i){
					         		messages.push({"id":item.id,"text":item.message,"status":item.status,"customerId":item.customerId,"msgType":item.msgType});
					         	});
					         	noticeStore.loadData(messages);
			         		}else if(messageData.result=="1001"){//被动通知未读
			         			Ext.Array.each(noticeStore.getRange(),function(item,i){
			         				messages.push(item);
			         			});
			         			
			         			Ext.Array.each(messageData.data,function(item,i){
					         		messages.push({"id":item.id,"text":item.message,"status":item.status,"customerId":item.customerId,"msgType":item.msgType});
					         	});
			         			noticeStore.loadData(messages);
			         		}
			         	}
		         	}
		         	/*console.info("来消息了===="+event.data);*/
		         };
            }
        }); 
    },
    destroy :function(){
    	webSocket.close();
    },
    ////////////////////////////日历/////////////////////////////////////////////////////////
    onListMonthChange: function (picker, date) {
        var me = this;
        var vm = this.getViewModel(), listNote = vm.getStore('listNote');
        var dateb = Ext.util.Format.date(date, "Y-m");
        crm.user.info({}, function (r, m, s) {
            if (s) {
                var listArray = [];
                crm.workBenchService.queryRemindInfo({ saleId: parseInt(r.userId), date: dateb }, function (r, m, s) {
                    if (s) {

                        for (var p in r) {
                             var  dateArray=[];
                                Ext.each(r[p], function (item, index, self) {
                                    if(parseInt(item.memoType)==0){
                                        item["editSta"]=0;
                                    }
                                    else{
                                          item["editSta"]=1;
                                    }
                                  
                                    dateArray.push(item);
                                });

                            listArray.push({ date: p.toString(), list: dateArray })
                        };
                        listNote.loadData(listArray);
                        Ext.defer(function () {
                            picker.fireEvent("fetchData", listNote, date);
                        }, 200);
                    }
                    else {
                        Ext.defer(function () {
                            picker.fireEvent("fetchData", listNote, date);
                        }, 200);
                    }
                }

                );
            }
        });
    },
    //日历事件编辑
    onEditOk: function (p,record) {
        var vm = this.getViewModel(), store = vm.getStore('listNote');
        var appView = crm.getApplication().viewport;
 
        appView.mask("保存中。。。");
        crm.customerService.saveOrUpdateMemoInfo({ msg: record.text, msgId: record.id, remindDate: record.date }, function (r, m, s) {
            if (s) {
                //Ext.Msg.alert('提示', m.Message == null ? '保存成功' : m.Message);
            }
            else{
             Ext.Msg.alert('提示', m.Message == null ? '保存失败!' : m.Message);
            }
            appView.unmask();
        });
        // 列表数据更新完毕，需要将更新的数据保存到服务器
    },
    onGetStore: function (marquee) {
        var store = this.getViewModel().getStore('notice');
        marquee.fireEvent('fetchData', store);
    },
    //日历事件删除 
    onDeleteNote: function (b,r) {
        var vm = this.getViewModel(), store = vm.getStore('listNote');
        var appView = crm.getApplication().viewport;
        appView.mask("删除中。。。");
        crm.customerService.removeMemoInfoByParams({ "id": r.id }, function (r, m, s) {
            if (s) {
                //Ext.Msg.alert('提示', m.Message == null ? '删除成功' : m.Message);
            }
            else{

                Ext.Msg.alert('提示', m.Message == null ? '删除失败!' : m.Message);
            }
            appView.unmask();
        });

    },
    //日历新增事件
    onAddNewNote: function (p,record) {
        var me = this,
            view = me.getView(),
            vm = view.getViewModel(),
            dataInstance,
            storeData,
            newRecord,
            recordExist = false;
        var appView = crm.getApplication().viewport;
        crm.customerService.saveOrUpdateMemoInfo({ msg: record.list.text, remindDate: record.date }, function (r, m, s) {
            if (s) {
            record.list.id=r.id;
            record.list.memoType="0";
            record.list.editSta=0;
            record.list.date=record.date;
            
            }else{
                Ext.Msg.alert('提示', m.Message == null ? '保存失败!' : m.Message);
            } 
          
        });

    },

    ///////////////////////////////////工作台顶部操作按钮/////////////////////////////////////////
    //消息弹窗
    showMessage: function () {
        var me = this, view = me.getView(),vm = view.getViewModel();
        var notice = vm.getStore('notice');
        var win = Ext.create('crm.view.work.message.Main', {"baseStore":notice,"baseView":view
        });
        // win.fireEvent('loadData');
        win.show();
    },
    //申请转移方案
    onTransferPlan: function () {
        var me = this, view = me.getView();
        Ext.create('crm.view.customer.customerList.TransferPlan', { parentObj: me }).show();
    },
    //添加记录
    addvisit: function () {
        var me = this, view = me.getView();
        var app = crm.getApplication(), viewport = app.viewport, userId = viewport.getViewModel().get('banner.userId');
        Ext.create('crm.view.customershared.customerInfo.ContactDetail', {
            Id: null,
            statusTemplateId: null,
            sales: userId,
            sign: 'list',
            roleName: null,
            mainType: "work-main"
        }).show();
    },
    //查重
    onCheck: function () {
        var me = this, view = me.getView(), vm = me.getViewModel();
        Ext.create('crm.view.customer.customerList.BatchCheck', { parentCustomerStore: null }).show();
    },
    ///////////////////////////////////我的排名/////////////////////////////////////////
    //我的排名团队数据加载
    teamDataload: function (userId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        teamStore = vm.getStore('teamStore');
        if (userId != "" && userId != null && userId != undefined) {
            teamStore.proxy.setExtraParam('saleId', parseInt(userId));
            teamStore.load();
        }
    },

    //我的排名 数据加载
    loadTeam: function (store, records, s) {
        if (s && records) {
            var me = this, view = this.getView();
        }
    },
    //我的排名 组内数据加载
    loadGroup: function (store, records, s) {
        if (s && records) {
            var me = this, view = this.getView();
            var groupbar = me.lookupReference('groupbar');
            if (records.length < 7) {
                groupbar.setVisible(true);
            }
            else {
                groupbar.setVisible(false);
            }
        }
    },
    //我的排名选项卡切换
    rankTabchange: function (tabPanel, newCard) {
        var me = this, view = this.getView();
        if (newCard.isLoad) {
            return;
        }
        switch (newCard.title) {
            case '组内':
                var vm = me.getViewModel(), groupStore = vm.getStore('groupStore');
                if (me.userId != "" && me.userId != null && me.userId != undefined) {
                    groupStore.proxy.setExtraParam('saleId', parseInt(me.userId));
                    groupStore.load();
                }
                break;
        }
        newCard.isLoad = true;
    },
    ///////////////////////////////////代办事项/////////////////////////////////////////
    //代办事项数据加载 
    matterDataload: function (userId) {
        var me = this, view = this.getView(), vm = me.getViewModel(),
            visitCustomerStore = vm.getStore('visitCustomerStore'),//拜访客户
            contractStore = vm.getStore('contractStore'),//合同
            expireCustomerStore = vm.getStore('expireCustomerStore');//到期客户
        var vistiArray = [],
            contractArray = [],
            expireArray = [];
        if (userId != "" && userId != null && userId != undefined) {
            var daibanwork = me.lookupReference('daibanwork');
            crm.workBenchService.queryNeedDoing({ saleId: userId }, function (result, option, success) {
                if (success) {
                    if (result) {
                        vistiArray.push({ id:'workbench_todayWaitVisitCustomer', hasLink: true,text: '今日待回访客户', value: result.remindOfDay });
                        vistiArray.push({ id:'workbench_thisWeekWaitVisitCustomer', hasLink: true,text: '本周待回访客户', value: result.remindOfWeek });
                        //visitCustomerStore.loadData(vistiArray);
                        contractArray.push({ id:'workbench_becomeDueContract', hasLink: true,text: '即将到期合同', value: result.expireContract });
                        contractArray.push({ id:'workbench_waitStartContract', hasLink: true,text: '待启动合同', value: result.startUpContract });
                        //contractStore.loadData(contractArray);
                        expireArray.push({ id:'workbench_NClassCustomer', hasLink: true,text: 'N类', value: result.customerStageMap.N });
                        expireArray.push({ id:'workbench_DClassCustomer', hasLink: true,text: 'D类', value: result.customerStageMap.D });
                        expireArray.push({ id:'workbench_CClassCustomer', hasLink: true,text: 'C类', value: result.customerStageMap.C });
                        expireArray.push({ id:'workbench_BClassCustomer', hasLink: true,text: 'B类', value: result.customerStageMap.B });
                        expireArray.push({ id:'workbench_AClassCustomer', hasLink: true,text: 'A类', value: result.customerStageMap.A });

                        vm.set("workbench_todayWaitVisitCustomer",me.returnArrayStr(result.remindOfDay_ids));
                        vm.set("workbench_thisWeekWaitVisitCustomer",me.returnArrayStr(result.remindOfWeek_ids));
                        vm.set("workbench_becomeDueContract",me.returnArrayStr(result.expireContract_ids));
                        vm.set("workbench_waitStartContract",me.returnArrayStr(result.startUpContract_ids));
                        vm.set("workbench_NClassCustomer",me.returnArrayStr(result.customerStageMap_ids.N_Ids));
                        vm.set("workbench_DClassCustomer",me.returnArrayStr(result.customerStageMap_ids.D_Ids));
                        vm.set("workbench_CClassCustomer",me.returnArrayStr(result.customerStageMap_ids.C_Ids));
                        vm.set("workbench_BClassCustomer",me.returnArrayStr(result.customerStageMap_ids.B_Ids));
                        vm.set("workbench_AClassCustomer",me.returnArrayStr(result.customerStageMap_ids.A_Ids));


               daibanwork.add(
                            {
                                xtype: 'weizhi-panel-CustomerStatisticsItem',
                                dataConfig: {
                                    itemData: {
                                        title: '待回访客户',
                                        label: '',
                                        value: 0,
                                        hasDetail: true,
                                        detailList:vistiArray
                                    },
                                    width: 220,
                                    height: 105,
                                    backgroundColor: 'rgb(201, 201, 201)',
                                    valueBgColor: 'rgb(234,238,241)',
                                    valueColor: 'rgb(255,153,102)',
                                    maxWidth: 370,
                                    minWidth: 100
                                },
                                showDetail: true,
                                listeners: {
                                    // 超链接事件
                                    cb: function (item) {
                                        var app = crm.getApplication(), viewport = app.viewport, appvm = viewport.getViewModel(),
                                            vm = me.getViewModel();
                                        //今日待回访客户跳转
                                        if(item.id=="workbench_todayWaitVisitCustomer"){
                                            appvm.set("workbench_ids",vm.get("workbench_todayWaitVisitCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }else if(item.id=="workbench_thisWeekWaitVisitCustomer"){
                                            //本周待回访客户跳转
                                            appvm.set("workbench_ids",vm.get("workbench_thisWeekWaitVisitCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }
                                    }
                                }
                            },
                            {
                                xtype: 'weizhi-panel-CustomerStatisticsItem',
                                dataConfig: {
                                    itemData: {
                                        title: '待处理合同',
                                        label: '',
                                        value: 0,
                                        hasDetail: true,
                                        detailList:contractArray
                                    },
                                    width: 220,
                                    height: 105,
                                    backgroundColor: 'rgb(201, 201, 201)',
                                    valueBgColor: 'rgb(234,238,241)',
                                    valueColor: 'rgb(255,153,102)',
                                    maxWidth: 370,
                                    minWidth: 100
                                },
                                showDetail: true,
                                listeners: {
                                    // 超链接事件
                                    cb: function (item) {
                                        var app = crm.getApplication(), viewport = app.viewport, appvm = viewport.getViewModel(),
                                            vm = me.getViewModel();
                                        //即将到期合同跳转
                                        if(item.id=="workbench_becomeDueContract"){
                                            appvm.set("workbench_ids",vm.get("workbench_becomeDueContract"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }else if(item.id=="workbench_waitStartContract"){
                                            //待启动合同跳转
                                            appvm.set("workbench_ids",vm.get("workbench_waitStartContract"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }
                                    }
                                }
                            },
                            {
                                xtype: 'weizhi-panel-CustomerStatisticsItem',
                                dataConfig: {
                                    itemData: {
                                        title: '即将到期客户',
                                        label: '',
                                        value: 0,
                                        hasDetail: true,
                                        detailList:expireArray
                                    },
                                    width: 400,
                                    height: 105,
                                    maxWidth: 500,
                                    minWidth: 100,
                                    backgroundColor: 'rgb(201, 201, 201)',
                                    valueBgColor: 'rgb(234,238,241)',
                                    valueColor: 'rgb(255,153,102)',
                                    labelLength: 3
                                },
                                showDetail: true,
                                listeners: {
                                    // 超链接事件
                                    cb: function (item) {
                                        var app = crm.getApplication(), viewport = app.viewport, appvm = viewport.getViewModel(),
                                            vm = me.getViewModel();
                                        //N类客户跳转
                                        if(item.id=="workbench_NClassCustomer"){
                                            appvm.set("workbench_ids",vm.get("workbench_NClassCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }else if(item.id=="workbench_DClassCustomer"){
                                            //D类客户跳转
                                            appvm.set("workbench_ids",vm.get("workbench_DClassCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }else if(item.id=="workbench_CClassCustomer"){
                                            //C类客户跳转
                                            appvm.set("workbench_ids",vm.get("workbench_CClassCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }else if(item.id=="workbench_BClassCustomer"){
                                            //B类客户跳转
                                            appvm.set("workbench_ids",vm.get("workbench_BClassCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }else if(item.id=="workbench_AClassCustomer"){
                                            //A类客户跳转
                                            appvm.set("workbench_ids",vm.get("workbench_AClassCustomer"));
                                            appvm.set("is_workbench_jump","yes");
                                            me.redirectTo('#user/customer-customerlist');
                                        }
                                    }
                                }
                            }
                        );
                    }
                }
            });
        }

    },

    onSave: function () {
        var me = this;
    },

    //关闭窗体
    recordCancel: function () {
        var me = this,
            mainView = me.getView();
        mainView.hide();
    },
    //保存方法
    recordSave: function () {
        var me = this,
            mainView = me.getView();
        mainView.hide();
    },


    ///////////////////////////////////销售客户统计/////////////////////////////////////////
    //销售客户统计、一览   (部门和用户权限)
    setAuthority: function (userId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        if (userId != "" && userId != null && userId != undefined) {
            crm.workBenchService.queryUserStage({ saleId: userId }, function (result, option, success) {
                if (success) {
                    if (result) {
                        //总监
                        if (result.userStage == 1) {
                            vm.set("salseDepDisabled", false);
                            vm.set("salseUserDisabled", false);
                             //拜访 计划 陪访计划
                            me.visitPlanDataload(result.userStage);
                            //销售客户部门数据加载
                            me.salesDepartmentDataload(me.userId);

                        }
                        else if (result.userStage == 2) {
                            vm.set("salseDepDisabled", true);
                            vm.set("salseUserDisabled", false);
                             //拜访 计划 陪访计划
                             me.visitPlanDataload(result.userStage);
                              //统计用户
                              me.salesUserStoreLoad(me.salesDepartmentId,me.userId);
                               //一览用户
                              me.glanceUserStoreLoad(me.glanceDeptId,me.userId);
                        }
                        else if (result.userStage == 3) {
                            vm.set("salseDepDisabled", true);
                            vm.set("salseUserDisabled", true);
                             //拜访 计划 陪访计划
                            me.visitPlanDataload(result.userStage);
                        }
                    }
                }
            });
        }
    },
    //拜访计划 陪访计划
   visitPlanDataload:function(userType){
    var me = this, view = this.getView(), vm = me.getViewModel();
    var visitTypestr="";
    var visitobj={};
    if(userType==1||userType==2){
      visitTypestr="陪访";
      visitobj={
                    title: '今日'+visitTypestr+'计划',
                    items: [{
                    	  xtype:'accompany-list',
                          height: 250,
                          dateType:1,// 1 今日  2 本周
                          name: 'accompany-list',
                          reference: 'accompany-list'
                    }]
                };
    }else if(userType==3){
          visitTypestr="拜访";
          visitobj= {
                       title: '今日'+visitTypestr+'计划',
                        items: [{
                                 xtype:'payvist-list',
                                 height: 250,
                                 dateType:1,// 1 今日  2 本周
                                 name: 'payvistlist',
                                 reference: 'payvistlist'
                       }]
                    };
    }
    
     var visitplanTb = me.lookupReference('visitplanTb');
     visitplanTb.add(visitobj,{
                                title: '本周'+visitTypestr+'计划',
                                items: [
                                
                                   ]
                            });
       me.lookupReference('visitplanTb').setActiveItem(0);
   }, 
   //拜访计划陪访计划
    visitplantabchange: function (tabPanel, newCard) {
        var me = this;
         var visitplanTb = me.lookupReference('visitplanTb');
        switch (newCard.title) {
            case '今日拜访计划':
               newCard.removeAll();
               newCard.add({
                                 xtype:'payvist-list',
                                 height: 250,
                                 dateType:1,// 1 今日  2 本周
                                 name: 'payvistlist',
                                 reference: 'payvistlist'
                       });
                break;
            case '本周拜访计划':
              newCard.removeAll();
               newCard.add({
                                 xtype:'payvist-list',
                                 height: 250,
                                 dateType:2,// 1 今日  2 本周
                                 name: 'payvistlist',
                                 reference: 'payvistlist'
                     });
                break;
            case '今日陪访计划':
             newCard.removeAll();
               newCard.add({
                          xtype:'accompany-list',
                          height: 250,
                          dateType:1,// 1 今日  2 本周
                          name: 'accompany-list',
                          reference: 'accompany-list'
                     });
             break;
            case '本周陪访计划':
               newCard.removeAll();
               newCard.add({
                          xtype:'accompany-list',
                          height: 250,
                          dateType:2,// 1 今日  2 本周
                          name: 'accompany-list',
                          reference: 'accompany-list'
                     });
                break;
        }
    },
    //销售客户统计部门数据加载
    salesDepartmentDataload: function (userId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        salesDepartmentStore = vm.getStore('salesDepartmentStore');
        if (userId != "" && userId != null && userId != undefined) {
            salesDepartmentStore.proxy.setExtraParam('saleId', parseInt(userId));
            salesDepartmentStore.load();
        }
    },
    //销售客户统计日期范围选择
    salesDateSelect: function (field, records) {
        var me = this,
            mainView = me.getView();
        if (records != null) {
            me.salesDayId = records.data.value;
            //数据加载
            me.salesDataload(me.userId, me.salesDayId, me.salesDepartmentId, me.salesUserId);
        }
    },

    //销售客户统计数据加载
    salesDataload: function (saleId, day, deptId, userId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        if (saleId == "" || saleId == null && saleId == undefined) {
            return;
        }

        var crmStatistics = me.lookupReference('panerCustomerStatistics');
        var newContractsArray = [];
        var newCustomersArray = [];
        var newSignProductInfoArray = [];
        crm.workBenchService.querySaleData({ saleId: saleId, day: day, deptId: deptId, userId: userId }, function (result, option, success) {
            if (success) {
                if (result) {
                    //客户
                    newCustomersArray.push({
                        text: 'N类新增',
                        value: result.newCustomersInfo.N == null ? 0 : result.newCustomersInfo.N,
                        handler: function(){
                            console.info("1111");
                        }
                    });
                    newCustomersArray.push({ text: 'D类新增', value: result.newCustomersInfo.D == null ? 0 : result.newCustomersInfo.D });
                    newCustomersArray.push({ text: 'C类新增', value: result.newCustomersInfo.C == null ? 0 : result.newCustomersInfo.C });
                    newCustomersArray.push({ text: 'B类新增', value: result.newCustomersInfo.B == null ? 0 : result.newCustomersInfo.B });
                    newCustomersArray.push({ text: 'A类新增', value: result.newCustomersInfo.A == null ? 0 : result.newCustomersInfo.A });
                    newCustomersArray.push({ text: 'V类新增', value: result.newCustomersInfo.V == null ? 0 : result.newCustomersInfo.V });
                    vm.set("workbench_newCustomers",me.returnArrayStr(result.newCustomers_Ids));
                    vm.set("workbench_newSignProductsCustomer",me.returnArrayStr(result.newSignProducts_ids));
                    vm.set("workbench_newContractsCustomer",me.returnArrayStr(result.newContracts_ids));
                    //产品
                    Ext.Array.each(result.newSignProductInfo, function (record) {
                        newSignProductInfoArray.push({ text: record.productName, value: record.num });
                    });
                    //合同
                    Ext.Array.each(result.newContractInfo, function (record) {
                        var typename = "";
                        if (record.contractType == 1) {
                            typename = "派遣";
                        }
                        else if (record.contractType == 2) {
                            typename = "代理";
                        }
                        else if (record.contractType == 3) {
                            typename = "其它";
                        }
                        newContractsArray.push({ text: typename, value: record.num });
                    });
                    var list = [
                        {
                            title: '新增客户',
                            label: '各阶段客户共新增',
                            value: result.newCustomers,
                            id:'workbench_newCustomers',
                            hasLink: true,
                            hasDetail: true,
                            detailList: newCustomersArray
                        },
                        {
                            title: '新签约产品',
                            label: '新签约产品共',
                            value: result.newSignProducts,
                            id:'workbench_newSignProductsCustomer',
                            hasLink: true,
                            hasDetail: true,
                            detailList: newSignProductInfoArray
                        },
                        {
                            title: '新签合同',
                            label: '新签合同共',
                            value: result.newContracts,
                            id:'workbench_newContractsCustomer',
                            hasLink: true,
                            hasDetail: true,
                            detailList: newContractsArray
                        }
                    ];
                    crmStatistics.fireEvent('onUpdateData', list);
                }
            }
        });
    },


    //销售客户统计部门选择
    salesDepartmentSelect: function (field, records) {
        var me = this,
            mainView = me.getView();
        if (records != null) {
            me.salesDepartmentId = records.data.id;
            me.salesUserStoreLoad(me.salesDepartmentId,me.userId);
            //数据加载
            me.salesUserId=null;
            me.lookupReference('salesUser').setValue(null);
            me.salesDataload(me.userId, me.salesDayId, me.salesDepartmentId, me.salesUserId);
        }
    },
    //销售客户统计用户选择
    salesUserSelect: function (field, records) {
        var me = this,
            mainView = me.getView();
        if (records != null) {
            me.salesUserId = records.data.id;
            //数据加载
            me.salesDataload(me.userId, me.salesDayId, me.salesDepartmentId, me.salesUserId);
        }
    },
    //销售客户统计用户数据加载
    salesUserStoreLoad: function (departmentId,saleId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        salesUserStore = vm.getStore('salesUserStore');
        salesUserStore.load({
            params: {
                deptId: departmentId,
                saleId:saleId
            },
            callback: function (result, op, success) {
                if (success) {
                    if (result && result.length > 0) {

                    }
                }
            }
        });
    },
    ///////////////////////////////////销售一览数据/////////////////////////////////////////
    //销售一览日期范围选择
    glanceDateSelect: function (field, records) {
        var me = this,
            mainView = me.getView();
        if (records != null) {
            me.glanceDay = records.data.value;
            var galcencetabpane = me.lookupReference('galcencetabpane');
            var acitveTab = galcencetabpane.getActiveTab();
            if (acitveTab.title == "扇形图") {
                me.shanxituAdditems(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
            }
            if (acitveTab.title == "柱状图") {
                me.getGlanceData(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
            }
        }
    },
    //销售一览部门选择
    glanceDepartmentSelect: function (field, records) {
        var me = this,
            mainView = me.getView();
        if (records != null) {
            me.glanceDeptId = records.data.id;
            me.glanceUserStoreLoad(me.glanceDeptId,me.userId);
            var galcencetabpane = me.lookupReference('galcencetabpane');
            var acitveTab = galcencetabpane.getActiveTab();
            me.glanceUserId=null;
            me.lookupReference('glanceSalesUser').setValue(null);
            if (acitveTab.title == "扇形图") {
                me.shanxituAdditems(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
            }
            if (acitveTab.title == "柱状图") {
                me.getGlanceData(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
            }

        }
    },
    //销售一览用户选择
    glanceUserSelect: function (field, records) {
        var me = this,
            mainView = me.getView();
        if (records != null) {
            me.glanceUserId = records.data.id;
            var galcencetabpane = me.lookupReference('galcencetabpane');
            var acitveTab = galcencetabpane.getActiveTab();
            if (acitveTab.title == "扇形图") {
                me.shanxituAdditems(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
            }
            if (acitveTab.title == "柱状图") {
                me.getGlanceData(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
            }

        }
    },
    //销售一览用户数据加载
    glanceUserStoreLoad: function (glanceDepartmentId,saleId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        glanceUserStore = vm.getStore('glanceUserStore');
        glanceUserStore.load({
            params: {
                deptId: glanceDepartmentId,
                saleId:saleId
            },
            callback: function (result, op, success) {
                if (success) {
                    if (result && result.length > 0) {

                    }
                }
            }
        });
    },
    //销售数据一览统计图选项卡切换
    salesCardtabchange: function (tabPanel, newCard) {
        var me = this;

        switch (newCard.title) {
            case '扇形图':
                me.shanxituAdditems(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
                break;
            case '柱状图':
                me.getGlanceData(me.userId, me.glanceDay, me.glanceDeptId, me.glanceUserId);
                break;
        }
        // newCard.isLoad = true;
    },
    //销售数据一览 扇形图选择效果
    chartCheckAll: function (com, r) {
        var me = this, vm = me.getViewModel();
        var stageitems = me.lookupReference('tagGroup').items;
        var validValue = [];
        var check = 0;
        var cmValue = com.inputValue;
        var checkValue = com.getRawValue();
        Ext.each(stageitems.items, function (item) {
            if (item.checked) {
                validValue.push(item.inputValue);
                check += 1;
            }
            if (check == 2) {
                return false;
            }
        });
        if (check == 2) {
            Ext.each(stageitems.items, function (item) {
                var cidIndex = validValue.indexOf(item.inputValue);
                if (cidIndex < 0) {
                    item.setDisabled(true);
                }
                else {
                    item.setDisabled(false);
                }
            });
        }
        else {
            Ext.each(stageitems.items, function (item) {
                var cidIndex = validValue.indexOf(item.inputValue);
                item.setDisabled(false);
            });
        }
        if (check == 2) {
            var shanxingtu = me.lookupReference('shanxingtu');
            shanxingtu.setHeight(500);
            shanxingtu.setScrollable(true);
            var pieitems = me.lookupReference('shanxingtu').items;
            Ext.each(pieitems.items, function (item) {
                var cidIndex = validValue.indexOf(item.tag);
                if (cidIndex < 0) {
                    item.setHeight(300);
                    item.setWidth(300);
                    item.setVisible(false);
                    me.objSetPosition(item, 1);
                }
                else {
                    item.setHeight(400);
                    item.setWidth(400);
                    item.setVisible(true);
                    //item.setPosition(100, -80);
                    me.objSetPosition(item, 2);
                }
            })
        }
        else {
            var shanxingtu = me.lookupReference('shanxingtu');
            shanxingtu.setHeight(300);
            var pieitems = me.lookupReference('shanxingtu').items;
            Ext.each(pieitems.items, function (item) {
                var cidIndex = validValue.indexOf(item.tag);
                item.setHeight(300);
                item.setWidth(300);
                item.setVisible(true);
                me.objSetPosition(item, 1);
            })
        }
        var c = 2;
    },
    //扇形图添加
    shanxituAdditems: function (userId, glanceDay, glanceDeptId, glanceUserId) {
        var me = this;
        var stageitems = me.lookupReference('tagGroup');
        stageitems.removeAll();
        var shanxingtu = me.lookupReference('shanxingtu');
        shanxingtu.removeAll();
        crm.workBenchService.queryPieCharSaleData({ saleId: userId, day: glanceDay, deptId: glanceDeptId, userId: glanceUserId }, function (result, option, success) {
            if (success) {
                if (result && result.length > 0) {
                    Ext.Array.each(result, function (record) {
                        stageitems.add({
                            boxLabel: record.groupName,
                            name: "group" + record.groupValue + "",
                            inputValue: record.groupValue,
                            handler: "chartCheckAll"
                        });
                        var pieArray = [];
                        var objarray = record.dataList;
                        for (var p in record.dataList) {
                            pieArray.push({name:p,data1:objarray[p]});
                        }

                        shanxingtu.add({
                            xtype: 'polar',
                            width: 300,
                            height: 300,
                            name: 'shanxingtu1',
                            reference: 'shanxingtu1',
                            insetPadding: 65,
                            innerPadding: 15,
                            tag: record.groupValue,
                            margin: '0 0 0 50',
                            sprites: [{
                                type: 'text',
                                text: record.groupName,
                                x: 30,
                                y: 280
                            }],
                            store: {
                                fields: ['name', 'data1'],
                                data: pieArray
                            },

                            series: {
                                type: 'pie',
                                highlight: true,
                                tooltip: {
                                    trackMouse: true,
                                    renderer: function (tooltip, record, item) {
                                        tooltip.setHtml(record.get('name') + ': ' + record.get('data1') + '%');
                                    }
                                },
                                angleField: 'data1',
                                label: {
                                    field: 'name',
                                    calloutLine: {
                                        length: 60,
                                        width: 3
                                    }
                                }
                            }
                        });
                    });
                    shanxingtu.setHeight(300);
                    var pieitems = me.lookupReference('shanxingtu').items;
                    Ext.each(pieitems.items, function (item) {
                        item.setHeight(300);
                        item.setWidth(300);
                        item.setVisible(true);
                        me.objSetPosition(item, 1);
                    });

                }
                else {
                    shanxingtu.add({
                        margin: '100 100 50 400',
                        xtype: 'displayfield',
                        value: '<span >当前无数据!</span>'
                    });
                }
            }
            else {
                shanxingtu.add({
                    margin: '100 100 50 400',
                    xtype: 'displayfield',
                    value: '<span>程序异常!</span>'
                });
            }
        });

    },
    //销售一览柱状图统计数据
    getGlanceData: function (userId, glanceDay, glanceDeptId, glanceUserId) {
        var me = this, view = this.getView(), vm = me.getViewModel();
        glanceBarChartStore = vm.getStore('glanceBarChartStore');
        glanceBarChartStore.load({
            params: {
                saleId: userId,
                day: glanceDay,
                deptId: glanceDeptId,
                userId: glanceUserId
            },
            callback: function (result, op, success) {
                if (success) {
                    if (result && result.length > 0) {
                    }
                }
            }
        });
    },
    //设置偏移
    objSetPosition: function (item, pietype) {
        var me = this,
            mainView = me.getView();
        if (window.screen.width < 1920) {
            if (pietype == 1) {
                item.setPosition(-80, -50);
            }
            else {
                item.setPosition(-20, -80);
            }

        }
        else {

            if (pietype == 1) {
                item.setPosition(-50, -50);
            }
            else {
                item.setPosition(100, -80);
            }
        }
    },
    onTradeinsurePlan:function(){
    	var me=this ,view=me.getView();
				Ext.create('crm.view.customer.customerList.Tradeinsure',{parentObj:me,type:'work-main'}).show();
    }



});
