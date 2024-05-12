// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
import {logger} from "firebase-functions";
import {onDocumentCreated} from "firebase-functions/v2/firestore";
import {onCall, HttpsError, onRequest} from "firebase-functions/v2/https";

// The Firebase Admin SDK to access Firestore.
import {getFirestore} from "firebase-admin/firestore";
import admin = require("firebase-admin");

admin.initializeApp();

const db = admin.firestore();

// Take the text parameter passed to this HTTP endpoint and insert it into
// Firestore under the path /messages/:documentId/original
exports.addmessage = onRequest(async (req, res) => {
    // Grab the text parameter.
    const original = req.query.text;
    // Push the new message into Firestore using the Firebase Admin SDK.
    const writeResult = await getFirestore()
        .collection("messages")
        .add({original: original});
    // Send back a message that we've successfully written the message
    res.json({result: `Message with ID: ${writeResult.id} added.`});
});

exports.getAllFoodDocuments = onCall((request) => {
    // Checking that the user is authenticated.
    if (!request.auth) {
        // Throwing an HttpsError so that the client gets the error details.
        throw new HttpsError("failed-precondition", "The function must be " +
            "called while authenticated.");
    }
    return new Promise((resolve, reject) => {
        let foods: any[] = [];
        db.collection("foods").get().then((snapshot) => {
            snapshot.forEach((doc) => {
                foods = foods.concat(doc.data());
            });
            console.log("foods result: " + JSON.stringify(foods, null, "\t"));
            resolve(foods);
        }).catch((error) => {
            console.log("db.collection(\"foods\").get gets err, reason: " +
             error);
            reject(error);
        });
    });
});

// Listens for new messages added to /messages/:documentId/original
// and saves an uppercased version of the message
// to /messages/:documentId/uppercase
exports.makeuppercase = onDocumentCreated("/messages/{documentId}", (event) => {
    // Grab the current value of what was written to Firestore.
    const original = event.data?.data().original;

    // Access the parameter `{documentId}` with `event.params`
    logger.log("Uppercasing", event.params.documentId, original);

    const uppercase = original.toUpperCase();

    // You must return a Promise when performing
    // asynchronous tasks inside a function
    // such as writing to Firestore.
    // Setting an 'uppercase' field in Firestore document returns a Promise.
    return event.data?.ref.set({uppercase}, {merge: true});
});
