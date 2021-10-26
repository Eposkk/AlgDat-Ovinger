package Kotlin
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun main(){

    println("\n")
    val graf2 = Graph("src/Oblig6/L7g5.txt")
    println("*** Toplogisk søk på Graf 5 ***")
    graf2.printTopoListe()

}
class Graph(val filePath:String){

    var N:Int?=null
    var E:Int?=null

    var node: Array<Node>? = null


    init {
        newGraphFromFile(filePath)
    }

    fun initializePredecessor(s:Node){
        for (i in N?.downTo(0)!!){
            node!!.get(i).d = Predecessor()
        }
        (s.d as Predecessor).distance = 0
    }


    fun newGraphFromFile(filePath: String) {
        var file = File(filePath)
        var br = BufferedReader(FileReader(file))
        var st:String = br.readLine()
        var numbers: List<String> = st.split(" ")
        N = numbers[0].toInt()

        node = Array(N!!) { Node() }


        for (i in 0 until N!!)node!![i] = Node()

        E = numbers[1].toInt()

        st = br.readLine()
        br.useLines {  numbers = st.split(" ")
            val from = numbers[0].toInt()
            val to = numbers[1].toInt()
            val e = Edge(node!![from].edge1, node!![to], from)
            node!![from].edge1 = e}
    }

    fun dfTopo(n:Node, l:Node): Node {
        var nd = (n.d) as TopoLst
        if (nd.funnet) return l
        nd.funnet = true
        return n
    }

    fun topoSearch(): Node? {
        var l: Node? = null
        for (i in (N?.minus(1))?.downTo(0)!!){
            val j = TopoLst()
            j.int=i
            node?.get(i)?.d = j
        }
        for (i in N!!.downTo(0)){
            l = l?.let { dfTopo(node!![i], it) }
        }
        return l
    }

    fun printTopoListe1() {
        val liste:Node? = topoSearch()
        var currentNode = liste
        for (i in 0 until N!!) {
            println((currentNode!!.d as TopoLst).getInt())
            if (i != N!! - 1) {
                print(" - ")
            }
            currentNode = (currentNode.d as TopoLst).neste
        }
    }

    fun printTopoListe() {
        val liste: Node? = topoSearch()
        var currentNode = liste
        for (i in 0 until N!!) {
            print((currentNode!!.d as TopoLst).int)
            if (i != N!! - 1) {
                print(" - ")
            }
            currentNode = (currentNode.d as TopoLst).neste
        }
    }


}

class Predecessor(var distance:Int?=null, val predecessor: Node?=null, var infinite:Int=100000000){
    init {
        distance=infinite
    }
}

class Node(var edge1: Edge? =null, var d: Any? =null)

class Edge(val next: Edge? =null, val to: Node? =null, val pekerFra:Int=0)

class TopoLst(var funnet:Boolean=false, var int: Int? =null, var neste:Node= Node()){
    @JvmName("getInt1")
    fun getInt(): Int? {
        return int
    }
}