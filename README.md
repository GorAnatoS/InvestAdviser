InvestAdviser
=============

InvestAdviser is a simple app written on Kotlin that demonstrates how to obtain stock base data from
https://www.moex.com/ and stock's additinal information from https://www.yahoo.com/.

Introduction
------------

InvestAdviser's GooglePlay page: https://play.google.com/store/apps/details?id=com.goranatos.investadviser

App's features
------------
You can:
* See current MOEX securies list with such data as Name, SecurityId, Current Price, Day value/percent price change
* Using search to find stock you need
* Organaze securitiy list by name, price, day price change
* Add security to your Portfolio and set share's quantity, price, date
* See additional share info like day range price, open price, volume, market cap, Beta, ROE, ROA
* See your portfolio purchase history and price change
* See simple analitics of your portfolio 

Screenshots
-----------

<table style="padding:20px">
  <tr>
    <td>
      <img src="screenshots/light_my_plants_linear.png"  alt="1" width = 260px>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <img src="screenshots/dark_my_plants_grid.png" alt="2" width = 260px >
    </td>
    <td width="30%">
      MyPlants Screen where you can:<br><br>
      - see your plant collection<br>
      - add new or edit plant<br> 
      - choose linear or grid types of layouts</td>
  </tr>
  
   <tr>
    <td>
      <img src="screenshots/light_edit_plant.png"  alt="1" width = 260px>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <img src="screenshots/dark_edit_plant.png" alt="2" width = 260px >
    </td>
    <td width="30%">
      EditPlant Screen where you can:<br><br>
      - choose picture or make photo of plant<br>
      - set Hibernate Mode when plants need other type of care (Winter)<br>
      - set Watering and Fertilization periods and next dates<br>
      - add plant's notes</td>
  </tr>
  
  <tr>
    <td>
      <img src="screenshots/light_plants_info.png"  alt="1" width = 260px>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <img src="screenshots/dark_plant_info.png" alt="2" width = 260px >
    </td>
    <td width="30%">
      PlantInfo Screen where you can:<br><br> 
      - see plant's info<br>
      - update watered and fertilized data<br>
    </td>
  </tr>
  
</table>

  
Libraries Used
--------------

* SharedPreferences
* ViewModel
* LiveData
* Room
* Navigation
* MaterialDesign
* Kotlin Android Coroutines
* Lifecycles
* Broadcasts
* Animations and Transitions
* [Firebase: Performance, Crashlitics, Analitics][0] for analize users app's performance, crashes and getting statistics
* [Hilt][1] for dependency injection
* [Glide][2] for image loading
* [Intro][3] on first start shop Intro's slides 
* [Permission][4] for getting permissions
* [lingver][5] for changing language
* [uCrop][6] for cropping image when getting plant's photos
* [groupie][7] for recycle view easy usage

[0]: https://firebase.google.com/
[1]: https://dagger.dev/hilt/
[2]: https://bumptech.github.io/glide/
[3]: https://github.com/AppIntro/AppIntro
[4]: https://github.com/permissions-dispatcher/PermissionsDispatcher
[5]: https://github.com/YarikSOffice/lingver
[6]: https://github.com/Yalantis/uCrop
[7]: https://github.com/lisawray/groupie

License
-------

  https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
