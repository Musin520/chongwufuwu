$(function (){
  $("#gloUrl").attr("gloUValue","http://localhost:7002")
  // 下面是网络环境地址
  // $("#gloUrl").attr("gloUValue","http://www.young33.top:7002")
  // $.ajax({
  //   url: Glo_BaseUrl + "/selectRentEquipment",
  //   type: "get",
  //   data: {
  //     "userId":Glo_user
  //   },
  //   success: function (data) {
  //
  //   },
  //   error: function () {
  //     alert("服务器异常！network error！")
  //   }
  // })
})
let  gloRequestUrl = "http://localhost:7002"
function getRole(){
  let Glo_BaseUrl = $("#gloUrl").attr("gloUValue")
  let Glo_user = localStorage.getItem("userId")
  let role = "";
  $.ajax({
    url: Glo_BaseUrl + "/sysuser/detail",
    type: "get",
    async:false,
    data: {
      "id":Glo_user
    },
    success: function (data) {
      role =  data.Data.role
    },
    error: function () {
      alert("服务器异常！network error！")
    }
  })
  return role;
}


function windowsAuto(code,msg){
  if (code === 200){
    swal("成功！", msg, "success");

  }else {
    swal("失败！", msg, "warning");

  }
}

//获取到角色，然后隐藏对应的菜单和按钮
function getRoleSetMenu(){
  let role = getRole()
  if (role === "1"){//管理员
    $(".userFun ").css({display:"none"})
    $(".adminUnDo ").css({display:"none"})
    $(".workerFun ").css({display:"none"})
  }else if (role === "2"){//用户
    $(".adminFun ").css({display:"none"})
    $(".workerFun ").css({display:"none"})
    $(".userUnDo").css({display:"none"})
  }else if (role === "3"){//3
    $(".adminFun ").css({display:"none"})
    $(".workUnDo ").css({display:"none"})
    $(".userFun").css({display:"none"})
  }
  return role
}

function handleTree(data, id, parentId, children) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children'
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}
