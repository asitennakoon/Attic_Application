import { Injectable } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/storage';

// firebase configurations
const firebaseConfig = {
  apiKey: "AIzaSyCU_hDV3QZMRcEJ9Z7piImaY48KqOvVlsI",
  authDomain: "attic-b6655.firebaseapp.com",
  projectId: "attic-b6655",
  storageBucket: "attic-b6655.appspot.com",
  messagingSenderId: "109489326265",
  appId: "1:109489326265:web:e502f9c6c27966d9447368",
  measurementId: "G-9DCTKH9SCF"
};

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  /* This service is used to send and retrieve photos and models from firebase storage. */

constructor(private storage: AngularFireStorage) { }

  //the name of the file and its' path in the device storage are required to upload.
  sendToFireStorage(filename: string, path: string){
    this.storage.upload(filename,path);
  }

  // a method to get an image or an object from firebase storage using its' path.

  // async getFromStore(path: string){
  //   let url ="gs://attic-b6655.appspot.com/images/Bedroom/Chair0.png";
  //   // return this.storage.refFromURL(url).getDownloadURL()
  //   let l
  //    this.storage.refFromURL(url).getDownloadURL().subscribe(link => {
  //     l = link;
  //     console.log(l);
  //     return l;
  //   });
  //   return l;
  // }

}



