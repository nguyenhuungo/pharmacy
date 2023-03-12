
    let sideBar = document.getElementsByClassName("nav-links");
    let links = document.getElementsByClassName("side-link");
    console.log(sideBar);
    for (let i = 0; i < links.length; i++) {
      links[i].addEventListener("click", function () {
      e.preventDefault();
      let currentLink = document.getElementsByClassName("active");
      console.log(currentLink);
      currentLink[0].className = currentLink[0].className.replace(" active", "");
      this.className += " active";
     });
    }