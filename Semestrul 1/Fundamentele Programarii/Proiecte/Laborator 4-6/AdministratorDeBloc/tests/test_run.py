from tests.test_apartment import *
from tests.test_list_manager import *
from tests.test_utils import *
from tests.test_validate import *

def test():
    test_add_cost_to_apartament()
    test_modify_cost()
    test_delete_all_costs()
    test_delete_specific_cost()
    test_delete_specific_cost_all()
    test_total_cost()
    test_costs_greater_than_sum()
    test_get_specific_cost()
    test_sort_by_cost_type()
    test_add_default_costs()
    test_filter_cost_list()
    test_get_specific_cost()
    test_costs_date_limit_sum()
    test_filter_cost_list_sum()
    test_copy_dict()
    test_undo()
    test_validate_cost()
    test_convert_to_int()
    test_convert_to_float()