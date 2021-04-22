import { Component, OnInit } from '@angular/core';
import { AngularFirestoreCollection } from '@angular/fire/firestore';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { LoginService } from 'src/app/Services/login.service';
import { IAccount } from '../../Interfaces/IAccount';



@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  /* This comonent is used for the login functionality */

  public accounts:IAccount[]=[];
  user: IAccount;
  
  public credentialsValid=true;
  public showContent=false;

  public loginForm: FormGroup;


  constructor(private loginService: LoginService) {
    // get all accounts from login service
    this.loginService.getAccounts().subscribe(data => {
      this.accounts=data;
    // console.log(data);

    });

    // login form controls and required validators initialization.
    this.loginForm = new FormGroup({
      'username': new FormControl('', [
        Validators.required
      ]),
      'password': new FormControl('', [
        Validators.required
      ])
    });


   }

  ngOnInit() {
    // console.log(this.accounts);

  }

  onSubmit():void{
    // check the login details for each account.
    this.accounts.forEach(account => {
      if(this.loginForm.value.username == account.username
          && this.loginForm.value.password==account.password
          ){
            this.credentialsValid=true;
            this.user=account;
            // console.log(this.user);
            // if the credentials match, login component will hiden and view products compnonent is loaded.
            this.showContent=true;
            return;
          }
    });

    this.credentialsValid=false;
      
  }



}
