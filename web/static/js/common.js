//打开字滑入效果
window.onload = function () {
    $(".connect p").eq(0).animate({"left": "0%"}, 600)
        .eq(1).animate({"left": "0%"}, 400);
};
//jquery.validate表单验证
$(document).ready(function () {
    let TL = 300;
    let pattern = /[0-9a-zA-Z_]+(\.[0-9a-zA-Z_]+)*@[0-9a-zA-Z_]+(\.[0-9a-zA-Z_]+)+/;
    var countdown = TL;

    function invokeSettime(obj) {
        settime(obj);

        function settime(obj) {
            if (countdown === 0) {
                $(obj).attr("disabled", false);
                $(obj).text("获取验证码");
                countdown = TL;
                return;
            }
            $(obj).attr("disabled", true);
            $(obj).text("" + countdown + " s 重新发送");
            countdown--;
            setTimeout(function () {
                    settime(obj)
                }
                , 1000);
        }
    }

    $("#verificationcode").click(function () {
        if ($("#registerForm").validate().element($(".email"))) {
            new invokeSettime(this);
        }
    });
    // $.validator.addMethod("myLoginVerify", function () {
    //     return false;
    // }, "用户名或密码不正确!");
//登陆表单验证
    $("#loginForm").validate({
        rules: {
            username: {
                required: true,//必填
            },
            password: {
                required: true,
                minlength: 6,
                maxlength: 20
            },
        },
        //错误信息提示
        messages: {
            username: {
                required: "必须填写用户名",
            },
            password: {
                required: "必须填写密码",
                rangelength: "用户名为8位数字"
            }
        },
        // onkeyup: false,
        // onfocusout: false,
        submitHandler: function (form) {
            $.ajax({
                type: "POST",
                dataType: "text",
                url: "/api/user/login",
                data: {
                    username: $(".username").val(),
                    password: $(".password").val()
                },
                success: function (result) {
                    if (result && result === 'true')
                        $(location).attr('href', '/home.html');
                    else {
                        $(".submitbtn").after("<label class='error'>用户名或密码错误!</label>");
                        setTimeout(function () {
                            $(".submitbtn").nextAll().remove();
                        }, 3000);
                    }
                },
                error: function () {
                    alert("error!");
                }
            });
        }


    });
//注册表单验证
    $("#registerForm").validate({
        rules: {
            username: {
                required: true,//必填
                rangelength: [8, 8],
                digits: true,
                remote: {
                    url: "/api/user/register/isnotduplicate",//用户名重复检查，别跨域调用
                    type: "post",
                    data: {
                        username: function () {
                            return $(".username").val();
                        }
                    }
                },
            },
            password: {
                required: true,
                minlength: 6,
                maxlength: 20,
            },
            email: {
                required: true,
                email: true,
                remote: {
                    url: "/api/user/register/isnotduplicate",//用户名重复检查，别跨域调用
                    type: "post",
                    data: {
                        email: function () {
                            return $(".email").val();
                        }
                    }
                }
            },
            confirm_password: {
                required: true,
                minlength: 3,
                equalTo: '.password'
            },
            veriinput: {
                required: true,
                minlength: 6,
                maxlength: 6
            }

        },
        //错误信息提示
        messages: {
            username: {
                required: "必须填写用户名",
                rangelength: "用户名为8为数字!",
                remote: "用户名不合法或已存在",
            },
            password: {
                required: "必须填写密码",
                minlength: "密码至少为6个字符",
                maxlength: "密码至多为20个字符",
            },
            email: {
                required: "你还未输入邮箱地址",
                email: "请输入正确的email地址",
                remote: "邮箱不合法或已存在",
            },
            confirm_password: {
                required: "请再次输入密码",
                minlength: "确认密码不能少于6个字符",
                equalTo: "两次输入密码不一致",//与另一个元素相同
            },
            veriinput: {
                required: "请输入验证码",
                minlength: "验证码为6位",
                maxlength: "验证码为6位"
            }
        },
        onkeyup: false,
        submitHandler: function (form) {
            var username =  $(".username").val();
            var password =   $(".password").val();
            $.ajax({
                type: "POST",
                dataType: "text",
                url: "/api/user/register/addanuser",
                data: {
                    username: username,
                    password: password,
                    email: $(".email").val(),
                    type: $(".type").val(),
                    vericode: $(".veriinput").val(),
                },
                success: function (result) {
                    if (result && result === '0') {
                        alert("注册成功！");
                        $.cookie('registeruser',username);
                        $.cookie('registerpassword',password);
                        $(location).attr('href', 'index.html');
                    }
                    else if(result === '1'){
                        alert("验证码错误！");
                    } else {
                        alert("注册失败！");
                    }
                },
                error: function () {
                    alert("error!");
                }
            });
        }
    });
    $(".getcode").click(function () {
        $.post("/api/user/register/getcode", {email: $(".email").val()});
    });
})
;
