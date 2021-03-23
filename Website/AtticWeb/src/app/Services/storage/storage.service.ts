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

  getFromStore(path: string){
    return this.storage.ref(firebaseConfig.storageBucket+"/images").child(path);
  }

}



