package com.example.myapplication.netutil.request
import org.json.JSONObject
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType


/**
 * 这是自己写的一个  json自动转 对象的工具类
 *
 * 本地其实就是     递归算法
 */
//类映射的对象
class MyGosn {
    fun <T> fromJsontoobject(json: String, clazz: Class<T>): T {  //类解析   外部使用的入口
        //通常json都有一个大括号
        val jsonobject = JSONObject(json)
        //获得一个无参实例
        val t = clazz.newInstance()
        //Log.i("测试入口","实例化对象成功了吗"+t)
        //得到全部的属性
        val fields = clazz.declaredFields
        //同时得到第一层json的键名称
        var keys = jsonobject.keys()
        //Log.i("测试入口","属性的数量"+fields.size)
        for (field in fields){
            keys.forEach {
                //  Log.i("测试入口","正在循环属性"+it)
                if (field.name.equals(it)){  //如果属性和键的名称相同 则把值附上
                    //赋值以后就立马判断 是不是一个类或者一个集合
                    // 一直递归 穷追到底
                    //Log.i("测试入口","找到相等属性"+it)
                  //  Log.i("哈哈","测试"+field.name+"是不是基本数据类型")
                    if (field.type.name.equals("java.lang.String")){   //是否是原始基本数据类型
                        //基本数据 就直接赋值就好了
                      //  Log.i("log","判断为原始数据"+it)
                       // Log.i("哈哈","数据内容"+jsonobject.getString())
                        field.set(t,jsonobject.getString(it))
                    }else if (field.type.name.equals("java.util.List")){  //判断属性是否为List
                   //     Log.i("测试one11","单个属性键集合的解析"+field.name)
                        val s=analysis(field,jsonobject)
                        //Log.i("log","有数据回来吗"+s)
                        field.set(t,s)
                    }else{   //都没有满足就是一个嵌套的内部类
                        //  Log.i("测试入口","判断为嵌套内部类"+it)
                        val cla = field.genericType
                        val  b = jsonobject.getJSONObject(it)
                        val s=  jsonOneClass(cla as Class<Any>,b)
                        //单个属性的赋值
                        field.set(t,s)
                    }

                }

            }
            keys = jsonobject.keys()
        //    Log.i("测试入口","属性实例化结束")
        }




        //需要留下来的是空值和集合

//        fields.forEach {//循环此对象的所有属性
//
//            //允许私有变量
//            it.isAccessible = true
//            if (it.type is List<*>){    //判断那个类的属性是不是一个集合
//                                        //如果属性有集合则采用另一种解析方
//            }
//        }
//        val t = clazz.newInstance()
//        val map = HashMap<String,Objects>()
//        cutString(json,map
//        val fields  =       clazz.declaredFields
//        fields.forEach {
//            it.isAccessible=true //设置可以访问私有变量
//            val keys: Set<String> = map.keys
//            for (key in keys) {
//                if (it.getName().equals(key)) {
//                    it.set(t, map[key]) //为变量名相同的赋值
//                }
//            }
//        }
      //  Log.i("测试","总体返回对象"+t)
        return t
    }


    //将json字符串 切歌成键值对
//    fun  cutString(json: String,map: HashMap<String,Objects>){
//
//        val json2= json.substring(1,json.length-1)//去掉头尾的括号
//    }
//}
    //解析集合属性并返回
    private fun analysis(field: Field,jsonObject: JSONObject):List<*>{
        //  val T = field.javaClass.newInstance() //实例化
        val l = mutableListOf<Any>()
        val fc = field.genericType
        if(fc is ParameterizedType){  //如果是泛型类参数
            val type = fc.actualTypeArguments[0]//得到一个泛型对象

                    //val filds = type.declaredFields//得到泛型类里面的属性
            //得到json的数组
            val jsonArray = jsonObject.getJSONArray(field.name)
            for (i in 0 until jsonArray.length()) {   //遍历每一个json数据
                val jsonObject2 = jsonArray.getJSONObject(i)  //遍历得到list集合的每一个数据
                        //这里输入单个json数据 单个对象的解析过程
              //  Log.i("log","遍历的对象"+type+"传过来的对象"+fc)
               // Log.i("log","得到泛型对象"+type)
                val t = jsonOneClass(type as Class<Any>,jsonObject2)
                //Log.i("log","有对象返回吗"+t as Student)
              //  Log.i("log","有对象返回吗")
                 l.add(t)  //把对象加到集合中去
            }

            // val t=fromJsontoobject(jsonObject.toString(), type as Class<Any>)
        }
        return l
    }



    //单个类的解析  且不是集合类型   进来的就只是单个类
    private fun <T>jsonOneClass(clazz: Class<T>,jsonObject: JSONObject):T{
        //判断是不是原始基本类
      //  //又该解析一个类了
     //   Log.i("测试one","开始单个属性的解析")
        val  t = clazz.newInstance()
     //   Log.i("测试one","当前的json数据"+jsonObject)
        val fields = clazz.declaredFields
        var keys = jsonObject.keys()

        for (field in fields){
       //     Log.i("测试two","单个属性变量的遍历 当前名字"+field.name+"当前的属性集合的大小"+fields.size+"当前json大小")
            for (it in keys) {
            //    Log.i("测试two","都循环了那些json数据"+it)
                if (it.equals(field.name)){
                  //  Log.i("测试three","当前的属性名"+field.name+"当前的json数据名"+it)
                    //进来了的话 就说明可以开始解析
                   // Log.i("哈哈","测试"+field.type.name.equals("java.lang.String")+"是不是基本数据类型d"+"嘿嘿"+field.type)
                    if (field.type.name.equals("java.lang.String")){   //是否是原始基本数据类型
                        //基本数据 直接赋值
//                            Log.i("测试one","单个属性原始数据类型"+it+"json数据"+jsonObject+"我需要的属性名字"+it)
//                            Log.i("测试one","json数据返回对了吗"+jsonObject.getString(it))
                        field.set(t,jsonObject.getString(it))

                    }else if (field.type.name.equals("java.util.List")){  //判断属性是否为List
                     //   Log.i("测试one11","单个属性键集合的解析"+field.name)
                        val s=analysis(field,jsonObject)
                      //  Log.i("log","有数据回来吗"+s)
                        field.set(t,s)
                    }else{   //都没有满足就是一个嵌套的内部类
                      //  Log.i("测试one","单个属性的嵌套内部类"+it+"它的类型为"+field.type)
                      //  Log.i("测试one","当前的json数据"+jsonObject)
                      //  Log.i("测试ondde","当前需要的值"+field.genericType)
                        val y = field.genericType
                        val b = jsonObject.getJSONObject(field.name)
                        val c = jsonOneClass(y as Class<Any>,b)
                        field.set(t,c)
                    }
                }
            }
            keys = jsonObject.keys()
        }

        //Log.i("测试one","单个返回对象之前"+t)

        return t
    }



//    fun isBasic(t:Any):Boolean{   //判断是不是一个基本类型
//        return t.javaClass.kotlin.simpleName
//    }

    //    fun isList(t:Any):Boolean{
//        return t is List<*>
//    }
    //再封装一个网络请求
//
//    override fun run() {
//        super.run()
//    }
}