import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  public title: string ="ADD PRODUCT";
  public membership: string ="Platinum Member";
  public name: string = "Sirisena Furniture Store";

  constructor() { }

  ngOnInit(): void {
  }

}
