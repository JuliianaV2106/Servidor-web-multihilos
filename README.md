# Servidor-web-multihilos

En esta seguna parte, afinaremos el servidor web, permitiendo que sea capaz de responder a la petición de diversos recursos: HTML (text/html) y archivos como imágenes (image/jpeg y image/gif)

En lugar de terminar el hilo después de mostrar en pantalla el mensaje de solicitud HTTP enviado por el browser, se analizará el mensaje de solicitud HTTP y se enviará una respuesta HTTP apropiada al browser. Se ignorará la información de las líneas de header y se se utilizará sólo el nombre del archivo que viene en la línea de solicitud.



Se asume que el mensaje de solicitud HTTP siempre utilizará el método GET, e ignorará que el cliente puede enviar otros métodos HTTP, como HEAD ó POST. En los lugares donde usted encuentre un signo de interrogación, ?, usted debe completar el código que hace falta.

🎯 Desarrolle el web server para que sea capaz de enviar recursos al cliente. El cliente pedirá el recurso por medio del HTTP Request. Por ejemplo, si el cliente solicita http://localhost:8080/index.html, el servidor será capaz de buscar en sus recursos el archivo index.html y lo enviará en el HTTP Response.

🎯 El server debería poder enviar también image/jpeg y image/gif. Esto lo podrá especificar con el header Content-Type.

🎯 Si el cliente pide un recurso, que el servidor no tenga, enviará un mensaje apropiado de respuesta para el HTTP 404
