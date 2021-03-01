import { Injectable } from '@angular/core';
import { IAccount } from '../Interfaces/IAccount';
import { AngularFirestore } from '@angular/fire/firestore';

@Injectable({
  providedIn: 'root'
})
export class LoginService {


constructor(private firestore: AngularFirestore) { 
 
}


getAccounts(){
  return this.firestore.collection<IAccount>('users').valueChanges();
}

addAccount(account: IAccount){
  this.firestore.collection('users').add(account); 
}

updateAccount(account: IAccount){
  let $key = account.$key;
  delete account.$key;
  if($key != undefined){
    this.firestore.doc('users/'+$key).update(account);
  }
}

deleteAccount($key: string){
  this.firestore.doc('users/'+$key).delete();
}

}