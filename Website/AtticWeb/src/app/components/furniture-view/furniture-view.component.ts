import { Component, Input, OnInit } from '@angular/core';
import { IFurniture } from 'src/app/Interfaces/IFurniture';
import { StorageService } from 'src/app/Services/storage/storage.service';

@Component({
  selector: 'app-furniture-view',
  templateUrl: './furniture-view.component.html',
  styleUrls: ['./furniture-view.component.css']
})
export class FurnitureViewComponent implements OnInit {

  @Input() furniture!: IFurniture;

  url: string = "";

  
  constructor(private storage: StorageService) {
    console.log(this.furniture)
   }

  ngOnInit() {
    console.log(this.furniture)
    let startIndex = this.furniture.scene.length + Number(2) +this.furniture.type.length;
    var url1 ="/"+this.furniture.scene+"/" + this.furniture.type + Number(this.furniture.$key.substring(startIndex));
    this.url=this.storage.getFromStore(url1)
  }

}
