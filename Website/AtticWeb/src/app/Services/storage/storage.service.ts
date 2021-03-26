import { Injectable } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/storage';
import { firebaseConfig } from '../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class StorageService {

constructor(private storage: AngularFireStorage) { }

  sendToFireStore(filename: string, path: string){
    this.storage.upload(filename,path);
  }

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



