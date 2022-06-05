function isAuthenticated(auth) {
    // console.log(auth);

    if(Object.keys(auth).length > 0) {
        return true;
    }
    return false;
}

export default isAuthenticated;