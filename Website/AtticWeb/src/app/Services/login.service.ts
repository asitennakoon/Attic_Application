import { Injectable } from '@angular/core';
import { IAccount } from '../Interfaces/IAccount';
import { AngularFirestore } from '@angular/fire/firestore';

@Injectable({
  providedIn: 'root'
})
export class LoginService {


constructor(private firestore: AngularFirestore) { 
 
}

/* retrieve all accounts from firebase
accounts are stored in firestore in firebase
each account object is of IAccount type. */
getAccounts(){
  return this.firestore.collection<IAccount>('users').valueChanges();
}

addAccount(account: IAccount){
  this.firestore.collection('users').add(account); 
}


/* ------------------------------- TODO ------------------------------ */


/* each account has a key, which is unique to an account
when updating an account, its' key must be deleted before updating.
Also the key is necessary to update the account. */

updateAccount(account: IAccount){
  let $key = account.$key;
  delete account.$key;
  if($key != undefined){
    this.firestore.doc('users/'+$key).update(account);
  }
}

/* To delete an account, only key is required. */

deleteAccount($key: string){
  this.firestore.doc('users/'+$key).delete();
}

}