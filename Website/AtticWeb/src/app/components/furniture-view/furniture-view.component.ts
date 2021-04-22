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

  /* This component is used to display a single furniture. */

  // Input decorator to get the furniture which needs to be displayed.
  @Input() furniture!: IFurniture;

  url = "../../../assets/images/1.png";


  constructor(private storage: StorageService, private storage1: AngularFireStorage) {

  }

  async ngOnInit() {

    // console.log(this.furniture.manufacturer)
    if (this.furniture != undefined) {
      let f: IFurniture = this.furniture;
      // console.log(f.manufacturer)

      // Folder path of "Bedroom-Modern-Chair38.png" in the firebase storage is,
      //  gs://attic-b6655.appspot.com/images/Bedroom/Modern/Chair38.png
      // get the start index of furniture number.(ex: Bedroom-Modern-Chair38 ==> start index = index of 3)
      let startIndex = f.scene.length + Number(1) + f.type.length;

      //scene contains the room type and style type separated by "-". 
      var splitIndex = Number(0);
      for(var i=0;i<f.scene.length;i++){
        if(f.scene.charAt(i) =="-"){
          splitIndex = i;
          break;
        }
      }
      var room = f.scene.substring(0,splitIndex);
      var style = f.scene.substring(splitIndex+1, f.scene.length+1);

      // console.log(room);
      // console.log(style);

      // combine to make the path
      var url1 = "/" + room + "/" + style+ "/" + f.type + Number(f.$key.substring(startIndex)) + ".jpg";
      // console.log(url1)

      // get the image from database asynchronously.
      await this.getFromStore(url1).subscribe(data=>{
        this.url =data;
        // console.log(data);
      });

      // console.log(this.url)
    }

  }


  // get the image from firebase storage.
   getFromStore(path: string){
      // Folder path of "Bedroom-Modern-Chair38.png" in the firebase storage is,
      //  gs://attic-b6655.appspot.com/images/Bedroom/Modern/Chair38.png
    let url ="gs://attic-b6655.appspot.com/images" +path;
   
    return  this.storage1.refFromURL(url).getDownloadURL()
    
   
  }

}