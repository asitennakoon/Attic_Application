import { Component, Input, OnInit } from '@angular/core';
import { IAccount } from 'src/app/Interfaces/IAccount';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input() details!:{title?: string ; account?: IAccount}

  public title: string ="" ;
  public membership: string ="";
  public name: string = "";

  constructor() {
    

   }

  ngOnInit(): void {
    if(this.details!= undefined){
      console.log(this.details);
      this.title=this.details.title;
      this.name=this.details.account.store;
      this.membership=this.details.account.type;
    }
  }

}
