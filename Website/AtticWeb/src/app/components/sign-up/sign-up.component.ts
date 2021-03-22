import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IAccount } from 'src/app/Interfaces/IAccount';
import { LoginService } from 'src/app/Services/login.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {


  
  public accounts:IAccount[]=[];
  user: IAccount;
  
  public credentialsValid: boolean=true;
  public error = ""
  public showContent=false;

  public signupForm: FormGroup;


  constructor(private loginService: LoginService, private router:  Router) {

    this.loginService.getAccounts().subscribe(data => {
      this.accounts=data;
    console.log(data);

    });


    this.signupForm = new FormGroup({
      'username': new FormControl('', [
        Validators.required
      ]),
      'email': new FormControl('', [
        Validators.email,
        Validators.required
      ]),
      'storeName': new FormControl('', [
        Validators.required
      ]),
      'password': new FormControl('', [
        Validators.required
      ]),
      'confirmPassword': new FormControl('', [
        Validators.required
      ])
    });


   }

  ngOnInit() {
    console.log(this.accounts);

  }

  onSubmit():void{
    console.log(this.signupForm.value)
    if(this.signupForm.valid){
      console.log("form valid")
      
     if( (!this.checkAccountExists()) && this.passwordValid()){
       this.credentialsValid =true;
       let value = this.signupForm.value;
        let newAccount: IAccount={
        username: value.username,
        password: value.password,
        store: value.storeName,
        email: value.email,
        type: "Platinum",
        }

        console.log(newAccount);
        this.loginService.addAccount(newAccount);
        this.router.navigateByUrl("");
        return; 

     }

     if(this.checkAccountExists()){
      this.error ="Account already exists ! "
      this.credentialsValid = false;
      return;
     }

     if(!this.passwordValid()){
      this.error ="Passwords doesn't match ! "
      this.credentialsValid = false;
     }
    }
      this.error ="Invalid email address ! "
      this.credentialsValid = false;
  }

  checkAccountExists(): boolean{
    for(let account of this.accounts){
      console.log(account)
      if(this.signupForm.value.username == account.username
          && this.signupForm.value.password==account.password
          ){     
    console.log("account exists")
          
            return true;
          }
      
    };
    console.log("account does not exist")

    return false;
  }

  passwordValid(): boolean{
    if( this.signupForm.value.password 
      == this.signupForm.value.confirmPassword){
    console.log("password match")
        
        return true;
    }
    
    
    return false;
  }

}
