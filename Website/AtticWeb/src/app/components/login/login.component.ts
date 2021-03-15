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

  public accounts:IAccount[]=[];

  
  public credentialsValid=true;
  public showContent=false;

  public loginForm: FormGroup;


  constructor(private loginService: LoginService) {

    this.loginService.getAccounts().subscribe(data => {
      this.accounts=data;
    console.log(data);

    });


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
    console.log(this.accounts);

  }

  onSubmit():void{

    this.accounts.forEach(account => {
      if(this.loginForm.value.username == account.username
          && this.loginForm.value.password==account.password
          ){
            this.credentialsValid=true;
            this.showContent=true;
            return;
          }
    });

    this.credentialsValid=false;
      
  }



}
