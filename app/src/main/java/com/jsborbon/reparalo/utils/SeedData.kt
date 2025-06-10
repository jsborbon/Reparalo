package com.jsborbon.reparalo.utils

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

suspend fun seedData() {
    val db = FirebaseFirestore.getInstance()

    val sampleDate = Timestamp.now()

    val clients = listOf(
        mapOf(
            "uid" to "UID_CLIENT_001",
            "name" to "Laura Gómez",
            "email" to "laura@example.com",
            "phone" to "600111222",
            "userType" to "CLIENT",
            "availability" to emptyList<Map<String, String>>(),
            "registrationDate" to sampleDate,
            "specialty" to null,
            "rating" to 0.0,
            "satisfaction" to 0.0,
            "completedServices" to 0,
            "favoriteCount" to 0,
            "lastServiceTimestamp" to 0L,
            "favorites" to emptyList<String>()
        ),
        mapOf(
            "uid" to "UID_CLIENT_002",
            "name" to "Carlos Sánchez",
            "email" to "carlos@example.com",
            "phone" to "600222333",
            "userType" to "CLIENT",
            "availability" to emptyList<Map<String, String>>(),
            "registrationDate" to sampleDate,
            "specialty" to null,
            "rating" to 0.0,
            "satisfaction" to 0.0,
            "completedServices" to 0,
            "favoriteCount" to 0,
            "lastServiceTimestamp" to 0L,
            "favorites" to emptyList<String>()
        )
    )

    val technicians = listOf(
        mapOf(
            "uid" to "UID_TECH_001",
            "name" to "Pedro Pérez",
            "email" to "pedro@example.com",
            "phone" to "611111111",
            "userType" to "TECHNICIAN",
            "availability" to listOf(
                mapOf("day" to "Lunes", "startTime" to "09:00", "endTime" to "12:00"),
                mapOf("day" to "Miércoles", "startTime" to "14:00", "endTime" to "18:00")
            ),
            "registrationDate" to sampleDate,
            "specialty" to "Electricidad",
            "rating" to 4.7,
            "satisfaction" to 0.0,
            "completedServices" to 0,
            "favoriteCount" to 0,
            "lastServiceTimestamp" to 0L,
            "favorites" to emptyList<String>()
        ),
        mapOf(
            "uid" to "UID_TECH_002",
            "name" to "Ana Ruiz",
            "email" to "ana@example.com",
            "phone" to "611222333",
            "userType" to "TECHNICIAN",
            "availability" to listOf(
                mapOf("day" to "Martes", "startTime" to "10:00", "endTime" to "13:00"),
                mapOf("day" to "Jueves", "startTime" to "15:00", "endTime" to "19:00")
            ),
            "registrationDate" to sampleDate,
            "specialty" to "Fontanería",
            "rating" to 4.8,
            "satisfaction" to 0.0,
            "completedServices" to 0,
            "favoriteCount" to 0,
            "lastServiceTimestamp" to 0L,
            "favorites" to emptyList<String>()
        )
    )

    val tutorials = listOf(
        mapOf(
            "id" to "tutorial_1",
            "title" to "Cambiar un enchufe",
            "description" to "Guía práctica para cambiar un enchufe sin riesgos.",
            "category" to "Electricidad",
            "difficultyLevel" to "Fácil",
            "estimatedDuration" to "20 min",
            "materials" to listOf("material_1", "material_2"),
            "videoUrl" to "https://youtu.be/abc123",
            "author" to "Pedro Pérez",
            "publicationDate" to Timestamp.now(),
            "averageRating" to 4.6,
        ),
        mapOf(
            "id" to "tutorial_2",
            "title" to "Reparar fuga de agua",
            "description" to "Cómo reparar una fuga simple en el fregadero.",
            "category" to "Fontanería",
            "difficultyLevel" to "Medio",
            "estimatedDuration" to "30 min",
            "materials" to listOf("material_3"),
            "videoUrl" to "https://youtu.be/xyz456",
            "author" to "Ana Ruiz",
            "publicationDate" to Timestamp.now(),
            "averageRating" to 4.3,
        ),
    )

    val categories = listOf(
        mapOf(
            "id" to "Electricidad",
            "name" to "Electricidad"
        ),
        mapOf(
            "id" to "Fontanería",
            "name" to "Fontanería"
        ),
        mapOf(
            "id" to "Carpintería",
            "name" to "Carpintería"
        )
    )

    val materials = listOf(
        mapOf(
            "id" to "material_1",
            "name" to "Destornillador",
            "quantity" to 1,
            "description" to "Tipo Philips estándar",
            "price" to 3.99,
        ),
        mapOf(
            "id" to "material_2",
            "name" to "Enchufe nuevo",
            "quantity" to 1,
            "description" to "Modelo universal",
            "price" to 5.50,
        ),
        mapOf(
            "id" to "material_3",
            "name" to "Llave inglesa",
            "quantity" to 1,
            "description" to "Para ajustar tuberías",
            "price" to 7.00,
        ),
    )

    val forumTopics = listOf(
        mapOf(
            "id" to "topic_1",
            "title" to "¿Qué cables usar para una instalación eléctrica segura?",
            "description" to "Estoy renovando el cableado y quiero recomendaciones.",
            "author" to "Pedro Pérez",
            "category" to "Electricidad",
            "date" to Timestamp.now(),
            "preview" to "Estoy renovando el cableado...",
            "comments" to 2,
            "likes" to 5,
            "views" to 40,
        ),
    )

    val serviceHistory = listOf(
        mapOf(
            "id" to "history_1",
            "userId" to "UID_CLIENT_001",
            "title" to "Instalación de lámpara LED",
            "description" to "El técnico Pedro instaló lámpara en el comedor.",
            "date" to System.currentTimeMillis(),
        ),
        mapOf(
            "id" to "history_2",
            "userId" to "UID_CLIENT_002",
            "title" to "Cambio de grifo",
            "description" to "Técnico Ana cambió el grifo del lavamanos.",
            "date" to System.currentTimeMillis(),
        ),
    )

    val favorites = listOf(
        mapOf(
            "userId" to "UID_CLIENT_001",
            "tutorialId" to "tutorial_1",
            "timestamp" to System.currentTimeMillis(),
        ),
        mapOf(
            "userId" to "UID_CLIENT_002",
            "tutorialId" to "tutorial_2",
            "timestamp" to System.currentTimeMillis(),
        ),
    )

    val comments = listOf(
        mapOf(
            "id" to "comment_1",
            "tutorialId" to "tutorial_1",
            "userId" to "UID_CLIENT_001",
            "content" to "Muy claro y útil, gracias!",
            "date" to Timestamp.now(),
            "rating" to 5,
        ),
        mapOf(
            "id" to "comment_2",
            "tutorialId" to "tutorial_2",
            "userId" to "UID_CLIENT_002",
            "content" to "Funcionó perfecto en casa.",
            "date" to Timestamp.now(),
            "rating" to 4,
        ),
    )

    try {
        (clients + technicians).forEach { user ->
            db.collection("users").document(user["uid"] as String).set(user).await()
        }

        tutorials.forEach { tutorial ->
            db.collection("tutorials").document(tutorial["id"] as String).set(tutorial).await()
        }

        materials.forEach { material ->
            db.collection("materials").document(material["id"] as String).set(material).await()
        }

        forumTopics.forEach { topic ->
            db.collection("forumTopics").document(topic["id"] as String).set(topic).await()
        }

        serviceHistory.forEach { history ->
            db.collection("serviceHistory").document(history["id"] as String).set(history).await()
        }

        favorites.forEach { fav ->
            db.collection("users")
                .document(fav["userId"] as String)
                .collection("favorites")
                .document(fav["tutorialId"] as String)
                .set(mapOf("timestamp" to fav["timestamp"]))
                .await()
        }

        comments.forEach { comment ->
            db.collection("comments").document(comment["id"] as String).set(comment).await()
        }

        categories.forEach { category ->
            db.collection("categories").document(category["id"] as String).set(category).await()
        }

        println("Firestore seeded")
    } catch (e: Exception) {
        println("Error seeding Firestore: ${e.message}")
    }
}
