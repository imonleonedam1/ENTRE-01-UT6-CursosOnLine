package programacion.entregaut6.modelo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Un objeto de esta clase mantiene una 
 * colección map que asocia  categorías (las claves) con
 * la lista (una colección ArrayList) de cursos que pertenecen a esa categoría 
 * Por ej. una entrada del map asocia la categoría 'BASES DE DATOS"' con
 * una lista de cursos de esa categoría
 * 
 * Las claves en el map se recuperan en orden alfabético y 
 * se guardan siempre en mayúsculas
 */
public class PlataformaCursos
{

    private final String ESPACIO = " ";
    private final String SEPARADOR = ":";
    private TreeMap<String, ArrayList<Curso>> plataforma;

    /**
     * Constructor  
     */
    public PlataformaCursos() {

        plataforma = new TreeMap<>();

    }

    /**
     * añadir un nuevo curso al map en la categoría indicada
     * Si ya existe la categoría se añade en ella el nuevo curso
     * (al final de la lista)
     * En caso contrario se creará una nueva entrada en el map con
     * la nueva categoría y el curso que hay en ella
     * Las claves siempre se añaden en mayúsculas  
     *  
     */
    public void addCurso(String categoria, Curso curso) {
        categoria = categoria.toUpperCase();
        if (plataforma.containsKey(categoria)) {
            ArrayList<Curso> aux = plataforma.get(categoria);
            aux.add(curso);
        } else {
            ArrayList<Curso> aux = new ArrayList<>();
            aux.add(curso);
            plataforma.put(categoria, aux);
        }
    }

    /**
     *  Devuelve la cantidad de cursos en la categoría indicada
     *  Si no existe la categoría devuelve -1
     *
     */
    public int totalCursosEn(String categoria) {
        if (plataforma.containsKey(categoria)) {
            ArrayList<Curso> aux = plataforma.get(categoria);
            return aux.size();
        }
        return -1;
    }

    /**
     * Representación textual de la plataforma (el map), cada categoría
     * junto con el nº total de cursos que hay en ella y a continuación
     * la relación de cursos en esa categoría (ver resultados de ejecución)
     * 
     * De forma eficiente ya que habrá muchas concatenaciones
     * 
     * Usar el conjunto de entradas y un iterador
     */
    public String toString() {
        Set<String> conjuntoClaves = plataforma.keySet();
        Iterator<String> it = conjuntoClaves.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            String categoria = it.next();
            sb.append(categoria).append(" (").append(totalCursosEn(categoria)).append(")\n");
            ArrayList<Curso> aux = plataforma.get(categoria);
            for (Curso c: aux) {
                sb.append(c.toString());
            }
        }
        return sb.toString();
    }

    /**
     * Mostrar la plataforma
     */
    public void escribir() {
        System.out.println(this.toString());
    }

    /**
     *  Lee de un fichero de texto la información de los cursos
     *  En cada línea del fichero se guarda la información de un curso
     *  con el formato "categoria:nombre:fecha publicacion:nivel"
     *  
     */
    public void leerDeFichero() {
        Scanner sc = new Scanner(
                this.getClass().getResourceAsStream("/cursos.csv"));
        while (sc.hasNextLine())  {
            String lineaCurso = sc.nextLine().trim();
            int p = lineaCurso.indexOf(SEPARADOR);
            String categoria = lineaCurso.substring(0, p).trim();
            Curso curso = obtenerCurso(lineaCurso.substring(p + 1));
            this.addCurso(categoria, curso);
        }
    }

    /**
     *  Dado un String con los datos de un curso
     *  obtiene y devuelve un objeto Curso
     *
     *  Ej. a partir de  "sql essential training: 3/12/2019 : principiante " 
     *  obtiene el objeto Curso correspondiente
     *  
     *  Asumimos todos los valores correctos aunque puede haber 
     *  espacios antes y después de cada dato
     */
    private Curso obtenerCurso(String lineaCurso) {
        String[] aux = lineaCurso.split(SEPARADOR);
        for (int i = 0; i < aux.length; i++) {
            aux[i] = aux[i].trim();
        }
        DateTimeFormatter formateador  = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(aux[1], formateador);
        Nivel nivel = Nivel.valueOf(aux[2].toUpperCase()); 
        Curso c = new Curso(aux[0], fecha, nivel);
        return c;
    }

    /**
     * devuelve un nuevo conjunto con los nombres de todas las categorías  
     *  
     */
    public TreeSet<String> obtenerCategorias() {
        Set<String> conjuntoClaves = plataforma.keySet();
        TreeSet<String> aux = new TreeSet<>();
        for (String clave: conjuntoClaves) {
            aux.add(clave);
        }
        return aux;
    }

    /**
     * borra de la plataforma los cursos de la categoría y nivel indicados
     * Se devuelve un conjunto (importa el orden) con los nombres de los cursos borrados 
     * 
     * Asumimos que existe la categoría
     *  
     */

    public TreeSet<String> borrarCursosDe(String categoria, Nivel nivel) {
        categoria = categoria.toUpperCase();
        ArrayList<Curso> array = plataforma.get(categoria);
        TreeSet<String> aux = new TreeSet<>();
        // for (Curso c: array) {
            // if (c.getNivel().equals(nivel)) {
                // array.remove(c);
            // }
        // }
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).getNivel().equals(nivel)) {
                aux.add(array.get(i).getNombre());
                array.remove(i);
            }
        }
        return aux;
    }

    /**
     *   Devuelve el nombre del curso más antiguo en la
     *   plataforma (el primero publicado)
     */

    public String cursoMasAntiguo() {
        LocalDate aux = LocalDate.MAX;
        Set<Map.Entry<String, ArrayList<Curso>>> conjuntoEntradas = plataforma.entrySet();
        for (Map.Entry<String, ArrayList<Curso>> entrada: conjuntoEntradas) {
            String clave = entrada.getKey();
            ArrayList<Curso> array = plataforma.get(clave);
            for (Curso c: array) {
                LocalDate fecha = c.getFecha();
                if (fecha.compareTo(aux) < 0) {
                    aux = fecha;
                }
            }
        }
        return String.valueOf(aux);
    }

    /**
     *  
     */
    public static void main(String[] args) {

        PlataformaCursos plataforma = new PlataformaCursos();
        plataforma.leerDeFichero();
        plataforma.escribir();

        System.out.println(
            "Curso más antiguo: " + plataforma.cursoMasAntiguo()
            + "\n");

        String categoria = "bases de datos";
        Nivel nivel = Nivel.AVANZADO;
        System.out.println("------------------");
        System.out.println(
            "Borrando cursos de " + categoria.toUpperCase()
            + " y nivel "
            + nivel);
        TreeSet<String> borrados = plataforma.borrarCursosDe(categoria, nivel);

        System.out.println("Borrados " + " = " + borrados.toString() + "\n");
        categoria = "cms";
        nivel = Nivel.INTERMEDIO;
        System.out.println(
            "Borrando cursos de " + categoria.toUpperCase()
            + " y nivel "
            + nivel);
        borrados = plataforma.borrarCursosDe(categoria, nivel);
        System.out.println("Borrados " + " = " + borrados.toString() + "\n");
        System.out.println("------------------\n");
        System.out.println(
            "Después de borrar ....");
        plataforma.escribir();

    }
}
