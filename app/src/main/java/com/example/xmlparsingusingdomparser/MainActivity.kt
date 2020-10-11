package com.example.xmlparsingusingdomparser

import android.os.Bundle
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.SAXException
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val userList: ArrayList<HashMap<String, String?>> = ArrayList()
            val lv = findViewById<ListView>(R.id.user_list)
            val istream: InputStream = assets.open("information.xml")
            val builderFactory: DocumentBuilderFactory = DocumentBuilderFactory.newInstance()
            val docBuilder: DocumentBuilder = builderFactory.newDocumentBuilder()
            val doc: Document = docBuilder.parse(istream)
            val nList: NodeList = doc.getElementsByTagName("user")
            for (i in 0 until nList.length) {
                if (nList.item(0).nodeType === Node.ELEMENT_NODE) {
                    val user: HashMap<String, String?> = HashMap()
                    val elm: Element = nList.item(i) as Element
                    user["name"] = getNodeValue("name", elm)
                    user["designation"] = getNodeValue("designation", elm)
                    user["location"] = getNodeValue("location", elm)
                    userList.add(user)
                }
            }

            val adapter: ListAdapter = SimpleAdapter(
                this,
                userList,
                R.layout.list,
                arrayOf("name", "designation", "location"),
                intArrayOf(R.id.name, R.id.designation, R.id.location)
            )
            lv.adapter = adapter
        }catch (e: IOException) {
            e.printStackTrace()
        }catch (e: ParserConfigurationException) {
            e.printStackTrace()
        }catch (e: SAXException) {
            e.printStackTrace()
        }
    }

    private fun getNodeValue(tag: String?, element: Element): String? {
        val nodeList = element.getElementsByTagName(tag)
        val node = nodeList.item(0)
        if (node != null) {
            if (node.hasChildNodes()) {
                val child = node.firstChild
                while (child != null) {
                    if (child.nodeType == Node.TEXT_NODE) {
                        return child.nodeValue
                    }
                }
            }
        }
        return ""
    }
}

