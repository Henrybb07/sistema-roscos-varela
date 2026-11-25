package poo.roscos;

public class Sesion {
    private static Usuario usuarioActual = null;

    private Sesion() { }

    public static void iniciar(Usuario u) { usuarioActual = u; }
    public static void cerrar() { usuarioActual = null; }

    public static Usuario getUsuario() { return usuarioActual; }
    public static boolean haySesion() { return usuarioActual != null; }
    public static Rol getRol() { return usuarioActual != null ? usuarioActual.getRol() : null; }

    public static boolean esAdmin()      { return getRol() == Rol.ADMINISTRADOR; }
    public static boolean esProduccion() { return getRol() == Rol.PRODUCCION; }
    public static boolean esVentas()     { return getRol() == Rol.VENTAS; }

    // ---- Atajos para decidir visibilidad de pestañas ----
    // Si por alguna razón el rol es nulo (error de tipeo en BD), le damos acceso total para que no se rompa la app.
    public static boolean puedeVerClientes()   { return esAdmin() || esVentas() || getRol() == null; }
    public static boolean puedeVerProduccion() { return esAdmin() || esProduccion() || getRol() == null; }
    public static boolean puedeVerInsumos()    { return esAdmin() || esProduccion() || getRol() == null; }
    public static boolean puedeVerPlaneacion() { return esAdmin() || esProduccion() || getRol() == null; }
    public static boolean puedeVerVentas()     { return esAdmin() || esVentas() || getRol() == null; }
}