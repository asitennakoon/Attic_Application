import { Component, Input, OnInit } from '@angular/core';
import { IAccount } from 'src/app/Interfaces/IAccount';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  /* This component is used to display the page header. */

  // input decorater to receive the page title and user account to display the user name and membership type.
  @Input() details!:{title?: string ; account?: IAccount}

  public title: string ="" ;
  public membership: string ="";
  public name: string = "";

  constructor() {
    

   }

  ngOnInit(): void {
    if(this.details!= undefined){
      // console.log(this.details);
      this.title=this.details.title;
      this.name=this.details.account.store;
      this.membership=this.details.account.type;
    }
  }

}
