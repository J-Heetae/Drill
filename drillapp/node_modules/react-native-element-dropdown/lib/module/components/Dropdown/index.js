function _extends() { _extends = Object.assign ? Object.assign.bind() : function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }
import _ from 'lodash';
import React, { useCallback, useEffect, useImperativeHandle, useMemo, useRef, useState } from 'react';
import { Dimensions, FlatList, I18nManager, Image, Keyboard, Modal, StyleSheet, Text, TouchableHighlight, TouchableWithoutFeedback, View, StatusBar } from 'react-native';
import { useDetectDevice } from '../../toolkits';
import { useDeviceOrientation } from '../../useDeviceOrientation';
import CInput from '../TextInput';
import { styles } from './styles';
const {
  isTablet
} = useDetectDevice;
const ic_down = require('../../assets/down.png');
const statusBarHeight = StatusBar.currentHeight || 0;
const DropdownComponent = /*#__PURE__*/React.forwardRef((props, currentRef) => {
  const orientation = useDeviceOrientation();
  const {
    testID,
    itemTestIDField,
    onChange,
    style = {},
    containerStyle,
    placeholderStyle,
    selectedTextStyle,
    itemContainerStyle,
    itemTextStyle,
    inputSearchStyle,
    iconStyle,
    selectedTextProps = {},
    data = [],
    labelField,
    valueField,
    searchField,
    value,
    activeColor = '#F6F7F8',
    fontFamily,
    iconColor = 'gray',
    searchPlaceholder,
    placeholder = 'Select item',
    search = false,
    maxHeight = 340,
    minHeight = 0,
    disable = false,
    keyboardAvoiding = true,
    inverted = true,
    renderLeftIcon,
    renderRightIcon,
    renderItem,
    renderInputSearch,
    onFocus,
    onBlur,
    autoScroll = true,
    showsVerticalScrollIndicator = true,
    dropdownPosition = 'auto',
    flatListProps,
    searchQuery,
    backgroundColor,
    onChangeText,
    confirmSelectItem,
    onConfirmSelectItem,
    accessibilityLabel,
    itemAccessibilityLabelField,
    mode = 'default'
  } = props;
  const ref = useRef(null);
  const refList = useRef(null);
  const [visible, setVisible] = useState(false);
  const [currentValue, setCurrentValue] = useState(null);
  const [listData, setListData] = useState(data);
  const [position, setPosition] = useState();
  const [keyboardHeight, setKeyboardHeight] = useState(0);
  const [searchText, setSearchText] = useState('');
  const {
    width: W,
    height: H
  } = Dimensions.get('window');
  const styleContainerVertical = useMemo(() => {
    return {
      backgroundColor: 'rgba(0,0,0,0.1)',
      alignItems: 'center'
    };
  }, []);
  const styleHorizontal = useMemo(() => {
    return {
      width: orientation === 'LANDSCAPE' ? W / 2 : '100%',
      alignSelf: 'center'
    };
  }, [W, orientation]);
  useImperativeHandle(currentRef, () => {
    return {
      open: eventOpen,
      close: eventClose
    };
  });
  useEffect(() => {
    return eventClose;
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  useEffect(() => {
    setListData([...data]);
    if (searchText) {
      onSearch(searchText);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data, searchText]);
  const eventOpen = () => {
    if (!disable) {
      setVisible(true);
      if (onFocus) {
        onFocus();
      }
      if (searchText.length > 0) {
        onSearch(searchText);
      }
      scrollIndex();
    }
  };
  const eventClose = useCallback(() => {
    if (!disable) {
      setVisible(false);
      if (onBlur) {
        onBlur();
      }
    }
  }, [disable, onBlur]);
  const font = useCallback(() => {
    if (fontFamily) {
      return {
        fontFamily: fontFamily
      };
    } else {
      return {};
    }
  }, [fontFamily]);
  const _measure = useCallback(() => {
    if (ref && ref !== null && ref !== void 0 && ref.current) {
      ref.current.measureInWindow((pageX, pageY, width, height) => {
        let isFull = isTablet ? false : mode === 'modal' || orientation === 'LANDSCAPE';
        if (mode === 'auto') {
          isFull = false;
        }
        const top = isFull ? 20 : height + pageY + 2;
        const bottom = H - top + height;
        const left = I18nManager.isRTL ? W - width - pageX : pageX;
        setPosition({
          isFull,
          width: Math.floor(width),
          top: Math.floor(top + statusBarHeight),
          bottom: Math.floor(bottom - statusBarHeight),
          left: Math.floor(left),
          height: Math.floor(height)
        });
      });
    }
  }, [H, W, orientation, mode]);
  const onKeyboardDidShow = useCallback(e => {
    _measure();
    setKeyboardHeight(e.endCoordinates.height);
  }, [_measure]);
  const onKeyboardDidHide = useCallback(() => {
    setKeyboardHeight(0);
    _measure();
  }, [_measure]);
  useEffect(() => {
    const susbcriptionKeyboardDidShow = Keyboard.addListener('keyboardDidShow', onKeyboardDidShow);
    const susbcriptionKeyboardDidHide = Keyboard.addListener('keyboardDidHide', onKeyboardDidHide);
    return () => {
      if (typeof (susbcriptionKeyboardDidShow === null || susbcriptionKeyboardDidShow === void 0 ? void 0 : susbcriptionKeyboardDidShow.remove) === 'function') {
        susbcriptionKeyboardDidShow.remove();
      }
      if (typeof (susbcriptionKeyboardDidHide === null || susbcriptionKeyboardDidHide === void 0 ? void 0 : susbcriptionKeyboardDidHide.remove) === 'function') {
        susbcriptionKeyboardDidHide.remove();
      }
    };
  }, [onKeyboardDidHide, onKeyboardDidShow]);
  const getValue = useCallback(() => {
    const defaultValue = typeof value === 'object' ? _.get(value, valueField) : value;
    const getItem = data.filter(e => _.isEqual(defaultValue, _.get(e, valueField)));
    if (getItem.length > 0) {
      setCurrentValue(getItem[0]);
    } else {
      setCurrentValue(null);
    }
  }, [data, value, valueField]);
  useEffect(() => {
    getValue();
  }, [value, data, getValue]);
  const scrollIndex = useCallback(() => {
    if (autoScroll && data.length > 0 && listData.length === data.length) {
      setTimeout(() => {
        if (refList && refList !== null && refList !== void 0 && refList.current) {
          const defaultValue = typeof value === 'object' ? _.get(value, valueField) : value;
          const index = _.findIndex(listData, e => _.isEqual(defaultValue, _.get(e, valueField)));
          if (index > -1 && index <= listData.length - 1) {
            var _refList$current;
            refList === null || refList === void 0 ? void 0 : (_refList$current = refList.current) === null || _refList$current === void 0 ? void 0 : _refList$current.scrollToIndex({
              index: index,
              animated: false
            });
          }
        }
      }, 200);
    }
  }, [autoScroll, data.length, listData, value, valueField]);
  const showOrClose = useCallback(() => {
    if (!disable) {
      if (keyboardHeight > 0 && visible) {
        return Keyboard.dismiss();
      }
      _measure();
      setVisible(!visible);
      setListData(data);
      if (!visible) {
        if (onFocus) {
          onFocus();
        }
      } else {
        if (onBlur) {
          onBlur();
        }
      }
      if (searchText.length > 0) {
        onSearch(searchText);
      }
      scrollIndex();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [disable, keyboardHeight, visible, _measure, data, searchText, scrollIndex, onFocus, onBlur]);
  const onSearch = useCallback(text => {
    if (text.length > 0) {
      const defaultFilterFunction = e => {
        var _$get;
        const item = (_$get = _.get(e, searchField || labelField)) === null || _$get === void 0 ? void 0 : _$get.toLowerCase().replace(' ', '').normalize('NFD').replace(/[\u0300-\u036f]/g, '');
        const key = text.toLowerCase().replace(' ', '').normalize('NFD').replace(/[\u0300-\u036f]/g, '');
        return item.indexOf(key) >= 0;
      };
      const propSearchFunction = e => {
        const labelText = _.get(e, searchField || labelField);
        return searchQuery === null || searchQuery === void 0 ? void 0 : searchQuery(text, labelText);
      };
      const dataSearch = data.filter(searchQuery ? propSearchFunction : defaultFilterFunction);
      setListData(dataSearch);
    } else {
      setListData(data);
    }
  }, [data, searchField, labelField, searchQuery]);
  const onSelect = useCallback(item => {
    if (confirmSelectItem && onConfirmSelectItem) {
      return onConfirmSelectItem(item);
    }
    if (onChangeText) {
      setSearchText('');
      onChangeText('');
    }
    onSearch('');
    setCurrentValue(item);
    onChange(item);
    eventClose();
  }, [confirmSelectItem, eventClose, onChange, onChangeText, onConfirmSelectItem, onSearch]);
  const _renderDropdown = () => {
    const isSelected = currentValue && _.get(currentValue, valueField);
    return /*#__PURE__*/React.createElement(TouchableWithoutFeedback, {
      testID: testID,
      accessible: !!accessibilityLabel,
      accessibilityLabel: accessibilityLabel,
      onPress: showOrClose
    }, /*#__PURE__*/React.createElement(View, {
      style: styles.dropdown
    }, renderLeftIcon === null || renderLeftIcon === void 0 ? void 0 : renderLeftIcon(visible), /*#__PURE__*/React.createElement(Text, _extends({
      style: [styles.textItem, isSelected !== null ? selectedTextStyle : placeholderStyle, font()]
    }, selectedTextProps), isSelected !== null ? _.get(currentValue, labelField) : placeholder), renderRightIcon ? renderRightIcon(visible) : /*#__PURE__*/React.createElement(Image, {
      source: ic_down,
      style: StyleSheet.flatten([styles.icon, {
        tintColor: iconColor
      }, iconStyle])
    })));
  };
  const _renderItem = useCallback(_ref => {
    let {
      item,
      index
    } = _ref;
    const isSelected = currentValue && _.get(currentValue, valueField);
    const selected = _.isEqual(_.get(item, valueField), isSelected);
    _.assign(item, {
      _index: index
    });
    return /*#__PURE__*/React.createElement(TouchableHighlight, {
      key: index.toString(),
      testID: _.get(item, itemTestIDField || labelField),
      accessible: !!accessibilityLabel,
      accessibilityLabel: _.get(item, itemAccessibilityLabelField || labelField),
      underlayColor: activeColor,
      onPress: () => onSelect(item)
    }, /*#__PURE__*/React.createElement(View, {
      style: StyleSheet.flatten([itemContainerStyle, selected && {
        backgroundColor: activeColor
      }])
    }, renderItem ? renderItem(item, selected) : /*#__PURE__*/React.createElement(View, {
      style: styles.item
    }, /*#__PURE__*/React.createElement(Text, {
      style: StyleSheet.flatten([styles.textItem, itemTextStyle, font()])
    }, _.get(item, labelField)))));
  }, [accessibilityLabel, activeColor, currentValue, font, itemAccessibilityLabelField, itemContainerStyle, itemTestIDField, itemTextStyle, labelField, onSelect, renderItem, valueField]);
  const renderSearch = useCallback(() => {
    if (search) {
      if (renderInputSearch) {
        return renderInputSearch(text => {
          if (onChangeText) {
            setSearchText(text);
            onChangeText(text);
          }
          onSearch(text);
        });
      } else {
        return /*#__PURE__*/React.createElement(CInput, {
          testID: testID + ' input',
          accessibilityLabel: accessibilityLabel + ' input',
          style: [styles.input, inputSearchStyle],
          inputStyle: [inputSearchStyle, font()],
          value: searchText,
          autoCorrect: false,
          placeholder: searchPlaceholder,
          onChangeText: e => {
            if (onChangeText) {
              setSearchText(e);
              onChangeText(e);
            }
            onSearch(e);
          },
          placeholderTextColor: "gray",
          iconStyle: [{
            tintColor: iconColor
          }, iconStyle]
        });
      }
    }
    return null;
  }, [accessibilityLabel, font, iconColor, iconStyle, inputSearchStyle, onChangeText, onSearch, renderInputSearch, search, searchPlaceholder, testID, searchText]);
  const _renderList = useCallback(isTopPosition => {
    const isInverted = !inverted ? false : isTopPosition;
    const _renderListHelper = () => {
      return /*#__PURE__*/React.createElement(FlatList, _extends({
        testID: testID + ' flatlist',
        accessibilityLabel: accessibilityLabel + ' flatlist'
      }, flatListProps, {
        keyboardShouldPersistTaps: "handled",
        ref: refList,
        onScrollToIndexFailed: scrollIndex,
        data: listData,
        inverted: isTopPosition ? inverted : false,
        renderItem: _renderItem,
        keyExtractor: (_item, index) => index.toString(),
        showsVerticalScrollIndicator: showsVerticalScrollIndicator
      }));
    };
    return /*#__PURE__*/React.createElement(TouchableWithoutFeedback, null, /*#__PURE__*/React.createElement(View, {
      style: styles.flexShrink
    }, isInverted && _renderListHelper(), renderSearch(), !isInverted && _renderListHelper()));
  }, [_renderItem, accessibilityLabel, flatListProps, listData, inverted, renderSearch, scrollIndex, showsVerticalScrollIndicator, testID]);
  const _renderModal = useCallback(() => {
    if (visible && position) {
      const {
        isFull,
        width,
        height,
        top,
        bottom,
        left
      } = position;
      const onAutoPosition = () => {
        if (keyboardHeight > 0) {
          return bottom < keyboardHeight + height;
        }
        return bottom < (search ? 150 : 100);
      };
      if (width && top && bottom) {
        const styleVertical = {
          left: left,
          maxHeight: maxHeight,
          minHeight: minHeight
        };
        const isTopPosition = dropdownPosition === 'auto' ? onAutoPosition() : dropdownPosition === 'top';
        let keyboardStyle = {};
        let extendHeight = !isTopPosition ? top : bottom;
        if (keyboardAvoiding && keyboardHeight > 0 && isTopPosition && dropdownPosition === 'auto') {
          extendHeight = keyboardHeight;
        }
        return /*#__PURE__*/React.createElement(Modal, {
          transparent: true,
          statusBarTranslucent: true,
          visible: visible,
          supportedOrientations: ['landscape', 'portrait'],
          onRequestClose: showOrClose
        }, /*#__PURE__*/React.createElement(TouchableWithoutFeedback, {
          onPress: showOrClose
        }, /*#__PURE__*/React.createElement(View, {
          style: StyleSheet.flatten([styles.flex1, isFull && styleContainerVertical, backgroundColor && {
            backgroundColor: backgroundColor
          }, keyboardStyle])
        }, /*#__PURE__*/React.createElement(View, {
          style: StyleSheet.flatten([styles.flex1, {
            width
          }, !isTopPosition ? {
            paddingTop: extendHeight
          } : {
            justifyContent: 'flex-end',
            paddingBottom: extendHeight
          }, isFull && styles.fullScreen])
        }, /*#__PURE__*/React.createElement(View, {
          style: StyleSheet.flatten([styles.container, isFull ? styleHorizontal : styleVertical, containerStyle])
        }, _renderList(isTopPosition))))));
      }
      return null;
    }
    return null;
  }, [visible, search, position, keyboardHeight, maxHeight, minHeight, dropdownPosition, keyboardAvoiding, showOrClose, styleContainerVertical, backgroundColor, containerStyle, styleHorizontal, _renderList]);
  return /*#__PURE__*/React.createElement(View, {
    style: StyleSheet.flatten([styles.mainWrap, style]),
    ref: ref,
    onLayout: _measure
  }, _renderDropdown(), _renderModal());
});
export default DropdownComponent;
//# sourceMappingURL=index.js.map