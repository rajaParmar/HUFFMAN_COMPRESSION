import java.io.*;
import java.util.*;

class node{
	private char t;
	public node left,right;
	private int freq;
	public node(char t,int f){
		this.t=t;
		this.left=null;
		this.right=null;
		this.freq=f;
	}

	public char get_char(){//returns character
		return this.t;
	}
	public int get_freq(){//returns frequency of current node
		return this.freq;
	}
	public void attach(char pos,node n){//attaches different nodes to current node
		if(pos=='l')
			this.left=n;
		else this.right=n;
	}



}
class HUFF_UTI{
	
	public int[] char_freq;//frequency of individual characters in the given file
	String file_name;//file name holder string variable
	Vector<node> Nodes; //Vector nodes initialisation
	private char[] code_buffer;private int pos=-1;
	
	public HUFF_UTI(String file_name){ //Constructor
		char_freq=new int[26];
		this.file_name=file_name;
		this.Nodes= new Vector<node>();
		this.code_buffer=new char[10000000];
	}

	public node combine(){ //call only once!
		if(Nodes.size()==1){
			return Nodes.elementAt(0);
		}
		else{
			node big=this.get_min();
			node small=this.get_min();
			node temp=new node('@',big.get_freq()+small.get_freq());
			temp.attach('l',big);
			temp.attach('r',small);
			Nodes.add(temp);
		}
		return combine();
	}
	private node get_min(){//returns minimum node and deletes it from the vector! //only for internal workings!
		int min=2147483647;
		int pos=-1;
		
		for(int i=0;i<Nodes.size();i++){
			int temp=Nodes.elementAt(i).get_freq();
			if(temp< min){
				min=temp;
				pos=i;
			}
			
		}
		node temp=Nodes.elementAt(pos);
		Nodes.remove(pos);
		return temp;
	}

	public void count_freq(){ //counts the frequency | call (1)
		try{
			FileReader f=new FileReader(file_name);
			int i;

			while((i=f.read())!=-1){
				if(i<97){
					i+=32;
				}
				char_freq[i%97]++;

			}
		}
		catch(IOException e){
			System.out.println(e);
		}
	}

	public void make_nodes(){ //Converts every character to a node and puts them in a Vector<node> | call (2)
		for(int i=97;i<=122;i++){
			if(char_freq[i%97]>=1)
			{
				Nodes.add(new node((char)(i),char_freq[i%97]));
			}
		}
	}

	public void print_nodes(){//printing function for debugging
		for(int i=0;i<Nodes.size();i++){
			System.out.println(Nodes.elementAt(i).get_char()+":"+Nodes.elementAt(i).get_freq());
		}
	}

	public void get_inorder(node root){
		if(root!=null){
			System.out.println(root.get_char()+" "+root.get_freq());
			get_inorder(root.left);
			get_inorder(root.right);
		}
		else System.out.println("null");
	}

	public void print_buffer(char[] buffer,int pos){
		for(int i=0;i<=pos;i++){
			System.out.print(buffer[i]);
		}
	}

	public void get_prefix_codes(node root){
		if(root!=null){
				if(root.get_char()!='@'){
					System.out.print(root.get_char()+"->");print_buffer(code_buffer,pos);System.out.println();
				}
				code_buffer[++pos]='0';
				get_prefix_codes(root.left);
				code_buffer[pos]='1';
				get_prefix_codes(root.right);
				code_buffer[pos--]='\u0000';
		}
	}
}


public class HUFF {
	public static void main(String[] args) {
		HUFF_UTI ins=new HUFF_UTI(args[0]);
		ins.count_freq();
		ins.make_nodes();
		node root=ins.combine();
		ins.get_inorder(root);
		ins.get_prefix_codes(root);
	}
}
