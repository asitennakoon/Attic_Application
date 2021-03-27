import { Component, Input, OnInit } from '@angular/core';
import { AngularFireStorage } from '@angular/fire/storage';
import { IFurniture } from 'src/app/Interfaces/IFurniture';
import { StorageService } from 'src/app/Services/storage/storage.service';

@Component({
  selector: 'app-furniture-view',
  templateUrl: './furniture-view.component.html',
  styleUrls: ['./furniture-view.component.css']
})
export class FurnitureViewComponent implements OnInit {

  @Input() furniture!: IFurniture;

  url = "../../../assets/images/1.png";


  constructor(private storage: StorageService, private storage1: AngularFireStorage) {

  }

  async ngOnInit() {

    console.log(this.furniture.manufacturer)
    if (this.furniture != undefined) {
      let f: IFurniture = this.furniture;
      // console.log(f.manufacturer)
      let startIndex = f.scene.length + Number(1) + f.type.length;

      var url1 = "/" + f.scene + "/" + f.type + Number(f.$key.substring(startIndex)) + ".png";
      console.log(url1)

      await this.getFromStore(url1).subscribe(data=>{
        this.url =data;
        console.log(data);
      });

      // console.log(this.url)
    }

  }



   getFromStore(path: string){
    let url ="gs://attic-b6655.appspot.com/images" +path;
   
    return  this.storage1.refFromURL(url).getDownloadURL()
    
   
  }

}
