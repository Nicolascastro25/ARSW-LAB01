
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW
## Ejercicio Introducción al paralelismo - Hilos - Caso BlackListSearch

# ARSW-LAB01
INTEGRANTES:
- Yesid Camilo Mora
- Jaime Nicolás Castro

### Dependencias:
####   Lecturas:
*  [Threads in Java](http://beginnersbook.com/2013/03/java-threads/)  (Hasta 'Ending Threads')
*  [Threads vs Processes]( http://cs-fundamentals.com/tech-interview/java/differences-between-thread-and-process-in-java.php)

### Descripción
  Este ejercicio contiene una introducción a la programación con hilos en Java, además de la aplicación a un caso concreto.
  

**Parte I - Introducción a Hilos en Java**

1. De acuerdo con lo revisado en las lecturas, complete las clases CountThread, para que las mismas definan el ciclo de vida de un hilo que imprima por pantalla los números entre A y B.

![image](https://user-images.githubusercontent.com/25957863/184039490-e43651da-1afb-43e8-9066-33966e38975b.png)

2. Complete el método __main__ de la clase CountMainThreads para que:
	1. Cree 3 hilos de tipo CountThread, asignándole al primero el intervalo [0..99], al segundo [99..199], y al tercero [200..299].
	
	![image](https://user-images.githubusercontent.com/25957863/184039828-9c629dd9-0bb8-4c50-bc3d-d4803edaa3bb.png)

	2. Inicie los tres hilos con 'start()'.

	![image](https://user-images.githubusercontent.com/25957863/184040846-156b2a06-78e4-4db0-b9b4-a468daa4ee71.png)

	3. Ejecute y revise la salida por pantalla.
	
	![image](https://user-images.githubusercontent.com/25957863/184040148-f29b8bbb-7f18-4594-bdce-21d4cec2aa7f.png)

	4. Cambie el incio con 'start()' por 'run()'. Cómo cambia la salida?, por qué?.

	!![image](https://user-images.githubusercontent.com/25957863/184040714-b84e95c1-6f96-43d8-b6d5-7dcf117af27d.png)
	![image](https://user-images.githubusercontent.com/25957863/184040423-0e4c9651-b230-4bd4-9aaa-d41a47f1e943.png)

La principal diferencia es que cuando el programa llama al método start (), se crea un nuevo subproceso y el código dentro del método run() se ejecuta en el nuevo subproceso, mientras que si llama al método run() directamente, no se crea un nuevo subproceso y el código dentro de run() se ejecutará en el hilo actual.

**Parte II - Ejercicio Black List Search**


Para un software de vigilancia automática de seguridad informática se está desarrollando un componente encargado de validar las direcciones IP en varios miles de listas negras (de host maliciosos) conocidas, y reportar aquellas que existan en al menos cinco de dichas listas. 

Dicho componente está diseñado de acuerdo con el siguiente diagrama, donde:

- HostBlackListsDataSourceFacade es una clase que ofrece una 'fachada' para realizar consultas en cualquiera de las N listas negras registradas (método 'isInBlacklistServer'), y que permite también hacer un reporte a una base de datos local de cuando una dirección IP se considera peligrosa. Esta clase NO ES MODIFICABLE, pero se sabe que es 'Thread-Safe'.

- HostBlackListsValidator es una clase que ofrece el método 'checkHost', el cual, a través de la clase 'HostBlackListDataSourceFacade', valida en cada una de las listas negras un host determinado. En dicho método está considerada la política de que al encontrarse un HOST en al menos cinco listas negras, el mismo será registrado como 'no confiable', o como 'confiable' en caso contrario. Adicionalmente, retornará la lista de los números de las 'listas negras' en donde se encontró registrado el HOST.

![](img/Model.png)

Al usarse el módulo, la evidencia de que se hizo el registro como 'confiable' o 'no confiable' se dá por lo mensajes de LOGs:

INFO: HOST 205.24.34.55 Reported as trustworthy

INFO: HOST 205.24.34.55 Reported as NOT trustworthy


Al programa de prueba provisto (Main), le toma sólo algunos segundos análizar y reportar la dirección provista (200.24.34.55), ya que la misma está registrada más de cinco veces en los primeros servidores, por lo que no requiere recorrerlos todos. Sin embargo, hacer la búsqueda en casos donde NO hay reportes, o donde los mismos están dispersos en las miles de listas negras, toma bastante tiempo.

Éste, como cualquier método de búsqueda, puede verse como un problema [vergonzosamente paralelo](https://en.wikipedia.org/wiki/Embarrassingly_parallel), ya que no existen dependencias entre una partición del problema y otra.

Para 'refactorizar' este código, y hacer que explote la capacidad multi-núcleo de la CPU del equipo, realice lo siguiente:

1. Cree una clase de tipo Thread que represente el ciclo de vida de un hilo que haga la búsqueda de un segmento del conjunto de servidores disponibles. Agregue a dicha clase un método que permita 'preguntarle' a las instancias del mismo (los hilos) cuantas ocurrencias de servidores maliciosos ha encontrado o encontró.

![image](https://user-images.githubusercontent.com/25957863/184043865-af70e649-fcce-4355-96e4-f5b07d8a5713.png)

2. Agregue al método 'checkHost' un parámetro entero N, correspondiente al número de hilos entre los que se va a realizar la búsqueda (recuerde tener en cuenta si N es par o impar!). Modifique el código de este método para que divida el espacio de búsqueda entre las N partes indicadas, y paralelice la búsqueda a través de N hilos. Haga que dicha función espere hasta que los N hilos terminen de resolver su respectivo sub-problema, agregue las ocurrencias encontradas por cada hilo a la lista que retorna el método, y entonces calcule (sumando el total de ocurrencuas encontradas por cada hilo) si el número de ocurrencias es mayor o igual a _BLACK_LIST_ALARM_COUNT_. Si se da este caso, al final se DEBE reportar el host como confiable o no confiable, y mostrar el listado con los números de las listas negras respectivas. Para lograr este comportamiento de 'espera' revise el método [join](https://docs.oracle.com/javase/tutorial/essential/concurrency/join.html) del API de concurrencia de Java. Tenga también en cuenta:

![image](https://user-images.githubusercontent.com/25957863/184044005-47ca3156-6180-45b8-80b0-87f1470d4cf5.png)

Dentro del método checkHost Se debe mantener el LOG que informa, antes de retornar el resultado, el número de listas negras revisadas VS. el número de listas negras total (línea 60). Se debe garantizar que dicha información sea verídica bajo el nuevo esquema de procesamiento en paralelo planteado.

Se sabe que el HOST 202.24.34.55 está reportado en listas negras de una forma más dispersa.

![image](https://user-images.githubusercontent.com/25957863/184044162-bf9a5a22-a29e-40d1-8b60-44d99e86f3f6.png)

Se sabe que el HOST 212.24.24.55 NO está en ninguna lista negra.

![image](https://user-images.githubusercontent.com/25957863/184044304-f94d8caa-d287-4677-ae32-d051193c1a3a.png)

Tiempo usando hilos:
	
![image](https://user-images.githubusercontent.com/25957863/184044433-844c0e64-6822-464f-943a-16eae73e2dfa.png)
	
Tiempo sin usar hilos:

![image](https://user-images.githubusercontent.com/25957863/184044444-5eeccdd4-f657-47b9-a6d3-dd527da3cebf.png)


**Parte II.I Para discutir la próxima clase (NO para implementar aún)**

La estrategia de paralelismo antes implementada es ineficiente en ciertos casos, pues la búsqueda se sigue realizando aún cuando los N hilos (en su conjunto) ya hayan encontrado el número mínimo de ocurrencias requeridas para reportar al servidor como malicioso. Cómo se podría modificar la implementación para minimizar el número de consultas en estos casos?, qué elemento nuevo traería esto al problema?


## PARTE III - EVALUACIÓN DE DESEMPEÑO
A continuación se mostrará la ejecución del programa en solution al problema de **Black List Search**, los resultados fueron
los siguientes:

### Ejecución con un solo Thread:
![](img/Thread_01.png)

### Ejecución con 6 Threads (Número de Núcleos de Procesamiento):
![](img/CoreThread.png)

### Ejecución con 12 Threads (Doble del Número de Núcleos de Procesamiento):
![](img/DoubleCoreThread.png)

### Ejecución con 50 Threads:
![](img/Thread_50.png)

### Ejecución con 100 Threads:
![](img/Thread_100.png)

1. Como podemos ver a mayo cantidad de hilos utilizados para la ejecución de este programa menor es la cantidad de tiempo
    requerida para la finalización del mismo. Pero si revisamos la Ley de Amdahls vemos que llegara en el que por más hilos
    que use el programa su tiempo de ejecución llegara al punto de no poderse reducir más. Esto se debe a los components que
    posea el computador/servidor que esté ejecutando el programa, ya que sus componentes son los que limitan la reducción de
    estos tiempos de ejecución asi usemos hasta 1.000 threads. 
   

2. Ahora, como podemos observar en las graficas, al usar la cantidad de hilos de procesamiento como nucleus hay observamos
    que hay una disminución significativa del tiempo de ejecución, pero si llegamos a duplicar esta cantidad de hilos la ejecución
   disminuye unos cuantos segundos más que en un proyecto de grandes escalas sería significativo. Además de observar esto, también
   vemos que el uso de la CPU disminuye de un 0.7% a un 0.0% al momento de doblar la cantidad de hilos.
   
3. En el caso hipotético de tener 100 maquinas y que cada una ejecutara un solo hilo no haríamos ni un buen uso de los
    equipos ni estaríamos aplicando correctamente la Ley de Amdahls, ya que no estamos sacando el maximo provecho de dichos 
   equipos, si tendríamos reducciones en la ejection de un programa, pero se podría reducir aún más. En cambio, si con 
   los supuestos 100 equipos utilizaríamos N hilos por equipo estaríamos sacando el maximo provecho de ello, ya que además de
   ejecutar diferentes partes de un programa en diferentes equipos también estaríamos ejecutando al tiempo diferentes
   en un solo equipo reduciendo asi el tiempo de ejecución de cada una de las partes del programa que esté corriendo cada uno
   de los equipos generando a nivel global del programa una reduction enorme en comparación a usar un solo hilo por equipo.
