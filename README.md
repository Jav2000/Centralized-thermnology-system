# Centralized-thermnology-system
Sistema centralizado de terminologías clínicas (Orphanet primer versión)

# Autores
* Javier Rodríguez Sánchez

# Entorno
* Spring Boot MVN
* Neo4j

# Spring Dependencies
* Thymeleaf
    * Motor de plantillas Java para aplicaciones web (Controllers)
* DevTools
    * Auto Refresh
* Spring Starter Web
    * Para crear aplicaciones web
* Spring data Neo4j
    * Base de datos
* Spring gson
    * Formato JSON para los grafos

# Documentacion
1. Nodos
  * Root:
    * Nodo raiz del grafo, sirve para obetener la clasifición de cada enfermedad.
  * Disorder:
    * Puede representar un grupo de enfermedades, una en concreto o un subtipo.
    * Campos:
      * orphaCode
      * name
      * group
      * type
      * description
      * synonyms
      * OMIM
      * averageAgeOnset
      * averageAgeOfDeath
      * inheritanceType
  * Gene:
    * Genes relacionados con las enfermedades del sistema.
    * Campos:
      * symbol
      * name
      * type
      * locus
      * OMIM
  * Phenotype:
    * Fenotipos relacionados con las enfermedades del sistema.
    * Campos:
      * HPOId
      * Term
2. Relaciones
  * ParentRelation:
    * Relacion entre enfermedades que construye su jerarquía.
    * Campos:
      * type
      * son
  * DisorderGeneRelation / GeneDisorderRelation:
    * Relaciones entre enfermedades y genes. En ambas direcciones para poder realizar diferentes tipos de búsquedas.
    * Campos:
      * type
      * status
      * gene / disorder
  * DisorderPhenotypeRelation / PhenotypeDisorderRelation:
    * Relaciones entre enfermedades y fenotipos. En ambas direcciones para poder realizar diferentes tipos de búsquedas.
    * Campos:
    	* frequency
      	* criteria
      	* phenotype / disorder
3. Repositorios
  * RootRepository:
  	* Neo4jRepository<Root, Long>
    ```
    	@Query("MATCH (r:Root) RETURN r")
	public Root findRoot();
	
	@Query("MATCH path = (r:Root)-[:PARENT_RELATION*1..1]->(d:Disorder) RETURN r, collect(relationships(path)), collect(d)")
	public Root findRootClassifications();
	
	@Query("MATCH path = (r:Root)-[:PARENT_RELATION*1..2]->(d:Disorder) RETURN r, collect(relationships(path)), collect(d)")
	public Root findRootDescendants();
    ```
  * DisorderRepository:
  	
    * UserRepository:
        * JPA Repository
        * `<User findByUsername(String username);>`
    * SecureTokenRepository:
        * JPA Repository
        * `<public SecureToken findByToken(final String token);>`
        * `<public Long removeByToken(String token);>`
    * TaxiRepository:
        * JPA Repository
    * TripRepository:
        * JPA Repository
3. Servicios
    * UserService y UserServiceImpl:
        * `public void save(User user);`
            * Método para crear un nuevo usuario. Encripta la contraseña recibida en texto plano
        * `public void saveWithoutEncripting(User user);`
            * Método para guardar un usuario sin encriptar su contraseña (UPDATE)
        * `public User findByUsername(String username);`
        * `public User findByEmail(String email);`
        * `public List<User> findAll();`
    * TaxiService y TaxiServiceImpl:
        * `public List<Taxi> findAll();`
    * EmailSenderService y EmailSenderServiceImpl:
        * `public EmailSenderServiceImpl(JavaMailSender javaMailSender);`
            * Método para inicializar en JavaMailSender
        * `public void sendEmail(SimpleMailMessage email);`
            * Método para enviar un SimpleMailMessage
    * TripService y TripServiceImpl:
        * `public List<Trip> findAll();`
4. Controladores
    * IndexController:
        * `public String indexAfterLogin(HttpServletRequest request);`
            * Redirecciona al usuario ya identificado según su rol.
    * LoginController:
        * `public String login(Model model);`
            * Devuelve la página login.html
        * `public String adminIndex(Model model);`
            * Devuelve la página de inicio de los Admin
        * `public String usersIndex(Model model);`
            * Devuelve la página de inicio de los User
    * RegistrationController:
        * `public ModelAndView displayRegistration(ModelAndView modelAndView, User user);` (GET)
            * Añade un objeto con formato User para el registro
            * Devuelve la vista para registrar un nuevo User
        * `public ModelAndView registration(ModelAndView modelAndView, User user);` (POST)
            * Registra un nuevo usuario
            * Si el email ya estaba registrado muestra vista error
            * Si no, pone el campo active en false, crea el usuario, genera un token nuevo asociado al user creado y envía un email con el link del token de verificación
        * `public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String token);`
            * Comprueba si el token está en la base de datos y activa el usuario asociado
            * Elimina el token recibido de la base de datos
    * UsuariosController:
        * `public String usuarios(Model model);`
            * Devuelve la página de Gestión de usuarios
    * TaxisController:
        * `public String taxis(Model model);`
            * Devuelve la página de Gestión de taxis
    * ViajesController:
        * `public String viajes(Model model);`
            * Devuelve la página de Gestión de viajes
    * AdministracionController:
        * `public String administracion(Model model);`
            * Devuelve la página principal de Administración
        * `public String administracionAdminRegister(Model model);`
            * Devuelve la página de registro de nuevos administradores
        * `public String administracionTaxiRegister(Model model);`
            * Devuelve la página de registro de nuevos taxis
        * `public ModelAndView adminRegistration(ModelAndView modelAndView, User user);`
            * Guarda el nuevo admin
        * `public ModelAndView taxiRegistration(ModelAndView modelAndView, Taxi taxi);`
            * Guarda el nuevo taxi
